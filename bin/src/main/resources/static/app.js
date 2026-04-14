const API_BASE = '/api/texts';

// DOM Elements
const createView = document.getElementById('createView');
const readView = document.getElementById('readView');
const textContent = document.getElementById('textContent');
const navShareBtn = document.getElementById('navShareBtn');

// Modal Elements
const shareModal = document.getElementById('shareModal');
const modalBackdrop = document.getElementById('modalBackdrop');
const closeModalBtn = document.getElementById('closeModalBtn');
const modalFormPhase = document.getElementById('modalFormPhase');
const modalResultPhase = document.getElementById('modalResultPhase');
const expirationSelect = document.getElementById('expirationSelect');
const sharePassword = document.getElementById('sharePassword');
const togglePasswordBtn = document.getElementById('togglePasswordBtn');
const togglePasswordIcon = document.getElementById('togglePasswordIcon');
const generateLinkBtn = document.getElementById('generateLinkBtn');
const finalShareLink = document.getElementById('finalShareLink');
const copyLinkBtn = document.getElementById('copyLinkBtn');
const copyTooltip = document.getElementById('copyTooltip');
const doneBtn = document.getElementById('doneBtn');

// Read View Elements
const passwordPrompt = document.getElementById('passwordPrompt');
const viewPassword = document.getElementById('viewPassword');
const unlockBtn = document.getElementById('unlockBtn');
const unlockError = document.getElementById('unlockError');
const contentDisplay = document.getElementById('contentDisplay');
const readonlyContent = document.getElementById('readonlyContent');
const copyContentBtn = document.getElementById('copyContentBtn');
const errorState = document.getElementById('errorState');
const errorTitle = document.getElementById('errorTitle');
const errorMessage = document.getElementById('errorMessage');

let currentViewId = null;

// Routing logic
window.addEventListener('hashchange', handleRoute);
document.addEventListener('DOMContentLoaded', handleRoute);

function handleRoute() {
    const hash = window.location.hash.substring(1);
    if (hash && hash.length > 0) {
        showReadView(hash);
    } else {
        showCreateView();
    }
}

function showCreateView() {
    createView.classList.remove('hidden');
    createView.classList.add('flex');
    readView.classList.add('hidden');
    readView.classList.remove('flex');
    navShareBtn.classList.remove('hidden');
    
    // Auto-focus logic with slight delay so transition doesn't look jittery
    setTimeout(() => textContent.focus(), 100);
}

async function showReadView(id) {
    createView.classList.add('hidden');
    createView.classList.remove('flex');
    readView.classList.remove('hidden');
    readView.classList.add('flex');
    navShareBtn.classList.add('hidden');

    // Hide all internal states first
    passwordPrompt.classList.add('hidden');
    contentDisplay.classList.add('hidden');
    errorState.classList.add('hidden');
    unlockError.classList.add('hidden');
    viewPassword.value = '';
    currentViewId = id;

    try {
        const response = await fetch(`${API_BASE}/${id}/metadata`);
        if (!response.ok) {
            const data = await response.json().catch(()=>({}));
            throw new Error(data.error || 'Text not found');
        }
        const metadata = await response.json();
        
        if (metadata.expired) {
            throw new Error('This link has expired.');
        }

        if (metadata.requiresPassword) {
            passwordPrompt.classList.remove('hidden');
            setTimeout(() => viewPassword.focus(), 100);
        } else {
            // Load content directly
            await loadContent(id, null);
        }

    } catch (err) {
        showError(err.message === 'Text not found' ? 'Not Found' : 'Link Unavailable', err.message);
    }
}

function showError(title, message) {
    passwordPrompt.classList.add('hidden');
    contentDisplay.classList.add('hidden');
    errorState.classList.remove('hidden');
    errorState.classList.add('flex');
    errorTitle.textContent = title;
    errorMessage.textContent = message;
}

async function loadContent(id, password) {
    try {
        const payload = password ? { password } : null;
        const res = await fetch(`${API_BASE}/${id}/access`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: payload ? JSON.stringify(payload) : null
        });

        const data = await res.json();
        
        if (!res.ok) {
            if (password) {
                unlockError.classList.remove('hidden');
                unlockError.textContent = data.error || 'Invalid password';
                return;
            } else {
                throw new Error(data.error || 'Access denied');
            }
        }

        passwordPrompt.classList.add('hidden');
        contentDisplay.classList.remove('hidden');
        contentDisplay.classList.add('flex');
        readonlyContent.textContent = data.content;

    } catch (err) {
        showError('Access Error', err.message);
    }
}

navShareBtn.addEventListener('click', () => {
    if (!textContent.value.trim()) {
        textContent.focus();
        // Visual shake feedback could be added here
        return;
    }
    openShareModal();
});

// Modal Actions
function openShareModal() {
    shareModal.classList.remove('hidden');
    // slight delay for transition
    setTimeout(() => {
        shareModal.classList.remove('opacity-0');
        const modalContentBox = document.getElementById('modalContentBox');
        modalContentBox.classList.remove('scale-95');
        modalContentBox.classList.add('scale-100');
    }, 10);
    
    // Reset modal state
    modalFormPhase.classList.remove('hidden');
    modalResultPhase.classList.add('hidden');
    expirationSelect.value = 'never';
    sharePassword.value = '';
    sharePassword.type = 'password';
    togglePasswordIcon.classList.replace('fa-eye-slash', 'fa-eye');
}

function closeShareModal() {
    shareModal.classList.add('opacity-0');
    const modalContentBox = document.getElementById('modalContentBox');
    modalContentBox.classList.remove('scale-100');
    modalContentBox.classList.add('scale-95');
    setTimeout(() => {
        shareModal.classList.add('hidden');
    }, 300);
}

closeModalBtn.addEventListener('click', closeShareModal);
modalBackdrop.addEventListener('click', closeShareModal);
doneBtn.addEventListener('click', closeShareModal);

togglePasswordBtn.addEventListener('click', () => {
    if (sharePassword.type === 'password') {
        sharePassword.type = 'text';
        togglePasswordIcon.classList.replace('fa-eye', 'fa-eye-slash');
    } else {
        sharePassword.type = 'password';
        togglePasswordIcon.classList.replace('fa-eye-slash', 'fa-eye');
    }
});

// Calculate Expiry Date string for backend
function calculateExpiry(val) {
    if (val === 'never') return null;
    const now = new Date();
    if (val === '10m') now.setMinutes(now.getMinutes() + 10);
    else if (val === '1h') now.setHours(now.getHours() + 1);
    else if (val === '1d') now.setDate(now.getDate() + 1);
    else if (val === '1w') now.setDate(now.getDate() + 7);
    return now.toISOString();
}

generateLinkBtn.addEventListener('click', async () => {
    const originalText = generateLinkBtn.innerHTML;
    generateLinkBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Initializing Secure Link...';
    generateLinkBtn.disabled = true;

    try {
        const payload = {
            content: textContent.value,
            expirationDate: calculateExpiry(expirationSelect.value),
            password: sharePassword.value.trim() || null
        };

        const res = await fetch(API_BASE, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!res.ok) throw new Error('Failed to create share link');

        const data = await res.json();
        
        // Show result
        modalFormPhase.classList.add('hidden');
        modalResultPhase.classList.remove('hidden');
        
        const link = `${window.location.origin}${window.location.pathname}#${data.id}`;
        finalShareLink.value = link;

    } catch (err) {
        alert(err.message);
    } finally {
        generateLinkBtn.innerHTML = originalText;
        generateLinkBtn.disabled = false;
    }
});

copyLinkBtn.addEventListener('click', async () => {
    try {
        await navigator.clipboard.writeText(finalShareLink.value);
        copyTooltip.style.opacity = '1';
        copyTooltip.style.transform = 'translate(-50%, -5px)';
        setTimeout(() => {
            copyTooltip.style.opacity = '0';
            copyTooltip.style.transform = 'translate(-50%, 0)';
        }, 2000);
    } catch (e) {
        finalShareLink.select();
        document.execCommand('copy');
    }
});

// View mode unlock
unlockBtn.addEventListener('click', () => {
    if (!currentViewId) return;
    const pwd = viewPassword.value;
    if(!pwd) {
        unlockError.classList.remove('hidden');
        unlockError.textContent = 'Password is required';
        return;
    }
    
    unlockError.classList.add('hidden');
    const origHtml = unlockBtn.innerHTML;
    unlockBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Decrypting...';
    unlockBtn.disabled = true;
    
    loadContent(currentViewId, pwd).finally(() => {
        unlockBtn.innerHTML = origHtml;
        unlockBtn.disabled = false;
    });
});

viewPassword.addEventListener('keyup', (e) => {
    if (e.key === 'Enter') unlockBtn.click();
});

copyContentBtn.addEventListener('click', async () => {
    try {
        await navigator.clipboard.writeText(readonlyContent.textContent);
        const origHtml = copyContentBtn.innerHTML;
        copyContentBtn.innerHTML = '<i class="fa-solid fa-check text-green-400"></i> Copied';
        setTimeout(() => {
            copyContentBtn.innerHTML = origHtml;
        }, 2000);
    } catch (e) {
         // fallback
         const el = document.createElement('textarea');
         el.value = readonlyContent.textContent;
         document.body.appendChild(el);
         el.select();
         document.execCommand('copy');
         document.body.removeChild(el);
    }
});
