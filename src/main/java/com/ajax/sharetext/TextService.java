package com.ajax.sharetext;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextService {

    @Autowired
    private TextRepository repository;

    public String saveText(Text text) {
        String url = UUID.randomUUID().toString().substring(0, 8);
        Text t = new Text();
        t.setUrl(url);
        t.setCreated(LocalDate.now());
        t.setUpdated(LocalDate.now());
        t.setText(text.getText());
        repository.save(t);
        return url;
    }

    public void updateText(Text text) {
        Long id = repository.findIdByUrl(text.getUrl());
        Optional<Text> optinalText = repository.findById(id);
        Text existtext = optinalText.get();
        existtext.setText(text.getText());
        existtext.setUpdated(LocalDate.now());
        repository.save(existtext);
    }

    public void updateExpiryDatePassword(Text text) {
        Long id = repository.findIdByUrl(text.getUrl());
        Optional<Text> optinalText = repository.findById(id);
        Text existtext = optinalText.get();
        if (!"".equals(text.getPassword())) {
            existtext.setPassword(text.getPassword());
            existtext.setHasPassword(true);
        }
        if (text.getExpiry() != null) {
            existtext.setExpiry(text.getExpiry());
        }
        repository.save(existtext);
    }

    public Text getTextByUrl(String url) {
        Text text = repository.findTextByUrl(url);
        if (text != null) {
            return text;
        } else {
            return null;
        }
    }

    public boolean findIfTextHasPassword(String url) {
        System.out.println("Checking if URL exists: hacking be");
        return repository.findIfTextHasPasswordByUrl(url);
    }

    public boolean checkPassword(Text text) {
        return text.getPassword().equals(repository.findPasswordByUrl(text.getUrl()));
    }

    public boolean existsByUrl(String url) {
        boolean exists = repository.existsByUrl(url);
        System.out.println("Checking if URL exists: " + url + " => " + exists);
        return exists;
    }
}
