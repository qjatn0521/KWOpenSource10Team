package com.example.myapplication.todo;

import java.util.Scanner;

public class TodoInput {
    private int id;
    private String todo_input;
    private int status;

    public TodoInput(String write){
        todo_input =write;
    }
    public int getId(){
        return id;
    }
    public void setId(){
        this.id=id;
    }

    public String getTodoInput(){
        return todo_input;
    }
    public void setTodo_input(String input){
        this.todo_input=input;
    }

    public int getStatus(){
        return status;
    }
    public void setStatus(int status){
        this.status=status;
    }

    public String getTodo_input(){
        return todo_input;
    }

}
