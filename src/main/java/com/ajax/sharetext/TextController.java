package com.ajax.sharetext;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TextController {

    @Autowired
    private TextService textService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/save")
    @ResponseBody
    public String saveText(@RequestBody Text text) {
        return textService.saveText(text);
    }

    @GetMapping("/{url}")
    @ResponseBody
    public String processURL(@PathVariable String url, Model model) {
        if (!textService.existsByUrl(url)) {
            model.addAttribute("textFound", false);
        } else {
            if (textService.findIfTextHasPassword(url)) {
                model.addAttribute("url", url);
                model.addAttribute("hasPassword", true);
            } else {
                Text text = textService.getTextByUrl(url);
                model.addAttribute("text", text);
                model.addAttribute("hasPassword", false);
            }
        }
        return "index";
    }

    @PostMapping("/update")
    public String updateText(@RequestBody Text text) {
        textService.updateText(text);
        return "index";
    }

    @PostMapping("/updateEP")
    public String updateExpiryDatePassword(@RequestBody Text text) {
        textService.updateExpiryDatePassword(text);
        return "index";
    }

    @PostMapping("/checkPassword")
    @ResponseBody
    public Text checkPassword(@RequestBody Text text) {
        if (textService.checkPassword(text)) {
            return textService.getTextByUrl(text.getUrl());
        } else {
            return null;
        }
    }

}
