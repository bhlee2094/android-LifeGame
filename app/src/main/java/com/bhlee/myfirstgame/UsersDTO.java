package com.bhlee.myfirstgame;

public class UsersDTO {
    private int money;
    private int hp;
    private int power;
    private int knowledge;

    public UsersDTO() {
    }

    public UsersDTO(int money, int hp, int power, int knowledge) {
        this.money = money;
        this.hp = hp;
        this.power = power;
        this.knowledge = knowledge;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(int knowledge) {
        this.knowledge = knowledge;
    }
}
