package com.example.elblog.controller;

import com.example.elblog.entity.Link;
import com.example.elblog.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 朱策
 */
@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping
    public ResponseEntity<Object> getLinks(Integer pageNum, Integer pageSize) {
        return new ResponseEntity<>(linkService.getLinks(pageNum,pageSize), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> listAll() {
        return new ResponseEntity<>(linkService.listAll(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addLink(@RequestBody Link link) {
        Integer id = link.getId();
        if(id == null){
            linkService.addLink(link);
        }else{
            linkService.editLink(link);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteById(Integer id){
        linkService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/batchDelete")
    public ResponseEntity<Object> batchDelete(@RequestBody Integer[] ids) {
        linkService.batchDelete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
