<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>ShareText</title>
        <link rel="icon" href="favicon.ico" type="image/x-icon">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="h-screen bg-gray-100 ">

        <div id="modal" class="hidden p-4 m-4 fixed inset-0 z-50 flex items-center justify-center sm:px-0" aria-labelledby="modal-title" role="dialog" aria-modal="true">

            <div class="fixed inset-0 bg-gray-500/75"></div>

            <div class="relative z-10 w-full max-w-md sm:my-8 bg-white rounded-2xl shadow-xl overflow-hidden">
                <div class="bg-white px-4 py-5 sm:px-6 sm:py-6 space-y-6">

                    <div class="space-y-4">

                        <div class="flex flex-col sm:flex-row sm:items-center gap-2">
                            <label for="date" class="sm:w-1/4 text-sm font-medium text-gray-700 whitespace-nowrap">Expiry Date</label>
                            <input type="date" id="date" name="date" class="flex-1 rounded-md border border-gray-300 p-2 text-sm w-full">
                        </div>

                        <div class="flex flex-col sm:flex-row sm:items-center gap-2" id="passwordSection">
                            <label for="password" class="sm:w-1/4 text-sm font-medium text-gray-700 whitespace-nowrap">Password</label>
                            <input type="password" id="password" class="flex-1 rounded-md border border-gray-300 p-2 text-sm w-full">
                        </div>

                        <div class="flex justify-end">
                            <button type="button" class="w-full bg-blue-600 text-white text-sm font-semibold px-4 py-2 rounded-lg shadow hover:bg-blue-700 transition-all">
                                Update
                            </button>
                        </div>

                    </div>

                    <div id="bar" class="flex flex-col sm:flex-row items-stretch sm:items-center border-t border-gray-200 pt-4 gap-2">  
                        <p id="url" class="p-2 flex-1 text-sm text-center truncate bg-gray-100 rounded-md"></p>
                        <button type="button" id="copyUrl" class="bg-red-500 px-4 py-2 text-sm font-semibold text-white shadow-xs rounded-lg hover:bg-red-600">Copy</button>
                    </div>

                </div>
            </div>
        </div>

        <div id="passwordModal" class="hidden fixed inset-0 z-50 flex items-center justify-center" aria-labelledby="modal-title" role="dialog" aria-modal="true">
            <div class="fixed inset-0 bg-gray-500/75"></div>

            <div class="relative z-10 w-full max-w-sm bg-white rounded-xl shadow-xl overflow-hidden">
                <div class="px-4 py-5 space-y-6">
                    <div class="space-y-4">
                        <div class="flex flex-col sm:flex-row sm:items-center gap-2">
                            <input type="password" id="checkPassword" placeholder="Password" class="flex-1 rounded-md border p-2 text-sm">
                        </div>

                        <div class="space-y-2">
                            <div class="flex justify-end">
                                <button type="button" id="unlock" class="w-full bg-blue-600 text-white text-sm px-4 py-2 rounded-lg hover:bg-blue-700">
                                    Unlock
                                </button>
                            </div>
                            <p id="errorMessage" class="hidden text-center text-sm text-red-500">
                                Incorrect Password.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <main class="max-w-4xl mx-auto p-4 my-4 bg-white shadow-md rounded-lg">
            <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-4 space-y-2 sm:space-y-0">

                <!-- Wrap the heading for better control -->
                <div class="w-full sm:w-auto text-center sm:text-left">
                    <h2 class="text-2xl sm:text-3xl font-semibold">
                        <a href="${pageContext.request.contextPath}">
                            <span class="text-red-500">Share</span><span class="text-gray-700">Text</span>
                        </a>
                    </h2>
                </div>

                <div class="flex space-x-2 w-full sm:w-auto justify-center sm:justify-end">
                    <button id="copy" class="w-full sm:w-auto px-4 py-2 bg-gray-200 rounded-md text-sm">Copy</button>
                    <button id="share" class="w-full sm:w-auto px-4 py-2 bg-gray-200 rounded-md text-sm">Share</button>
                </div>
            </div>

            <textarea id="text" class="h-80 sm:h-96 w-full border p-4 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"></textarea>
        </main>

        <script>
//
//            const $modal = $('#modal');
//            const mPassword = $('#modalPassword');
//
//            $(window).on('click', function (event) {
//                if ($(event.target).is($modal)) {
//                    $modal.addClass('hidden');
//                }
//            });
//
//            $(window).on('click', function (event) {
//                if ($(event.target).is(mPassword)) {
//                    mPassword.addClass('hidden');
//                }
//            });
//
//            $('#share', 'modalPassword').on('click', function () {
//                $('#modal').removeClass('hidden');
//            });
//
//            $(document).ready(function () {
//            <c:if test="${hasPassword}">
//                $('#passwordModal').removeClass('hidden');
//            </c:if>
//
//            <c:if test="!${textFound}">
//                alert("Text Not Found");
//            </c:if>
//
//                $('#unlock').click(function () {
//                    $.ajax({
//                        url: '${pageContext.request.contextPath}/checkPassword',
//                        type: 'POST',
//                        contentType: 'application/json',
//                        data: JSON.stringify({url: '${url}', password: $('#checkPassword').val()}),
//                        dataType: 'text',
//                        success: function (data) {
//                            if (data === '') {
//                                $('#errorMessage').show();
//                            } else {
//                                var parsedData = JSON.parse(data);
//                                $('#passwordModal').hide();
//                                $('#text').val(parsedData.text);
//                            }
//                        },
//                        error: function (error) {
//                            console.error('Error submitting data: ', error);
//                            alert('Error occurred while generating URL.');
//                        }
//                    });
//                });
//
//                var urlPath = window.location.pathname.trim();
//                if (urlPath.length > 11) {
//                    $.ajax({
//                        url: url, // sample API endpoint
//                        type: 'GET', // or 'POST', 'PUT', etc.
//                        dataType: 'json', // expected response data type
//                        success: function (response) {
//                            console.log('Success:', response);
//                        },
//                        error: function (xhr, status, error) {
//                            console.error('Error:', error);
//                        }
//                    });
//
//                    $('#share').hide();
//                }
//
//                $('#copy').on('click', function () {
//                    navigator.clipboard.writeText($('#text').val());
//                });
//
//                let debounceTimeout;
//
//                $('#text').on('click', function () {
//                    var text = $(this).val();
//                    var url = window.location.pathname.trim();
//                    clearTimeout(debounceTimeout);
//                    debounceTimeout = setTimeout(function () {
//                        if (url.length > 11) {
//                            var data = {url: '${text.url}', text: text};
//                            $.ajax({
//                                url: '${pageContext.request.contextPath}/update',
//                                type: 'POST',
//                                contentType: 'application/json',
//                                data: JSON.stringify(data)
//                            });
//                        }
//                    }, 2000);
//                });
//
//            });
//
//            $('#share').on('click', function () {
//                const text = $('#text').val();
//                $.ajax({
//                    url: '${pageContext.request.contextPath}/save',
//                    type: 'POST',
//                    contentType: 'application/json',
//                    data: JSON.stringify({text: text}),
//                    dataType: 'text',
//                    success: function (url) {
//                        $('#url').text(window.location.href + url);
//                        $modal.removeClass('hidden');
//                    },
//                    error: function (error) {
//                        console.error('Error submitting data: ', error);
//                        alert('Error occurred while generating URL.');
//                    }
//                });
//            });
//
//            $(document).on('click', function (event) {
//                const $modalContent = $modal.find('.rounded-2xl');
//                if ($modal.is(':visible') && !$modalContent.is(event.target) && $modalContent.has(event.target).length === 0) {
//                    $modal.addClass('hidden');
//                }
//            });
//
//            $('#copyUrl').on('click', function () {
//                navigator.clipboard.writeText($('#url').text());
//            });

            $(document).ready(function () {
                var urlPath = window.location.pathname.trim();
                if (urlPath.length > 11) {
                    $.ajax({
                        url: 'http://localhost:8080/ShareText/730a8fdb', // sample API endpoint
                        type: 'GET', // or 'POST', 'PUT', etc.
                        dataType: 'json', // expected response data type
                        success: function (response) {
                            console.log('Success:', response);
                        },
                        error: function (xhr, status, error) {
                            console.error('Error:', error);
                        }
                    });

                    $('#share').hide();
                }
            });


        </script>
    </body>
</html>
