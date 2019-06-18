package me.abhi.arcade.manager;

public class Manager {

    public ManagerHandler managerHandler;

    protected Manager(ManagerHandler managerHandler) {
        this.managerHandler = managerHandler;
    }
}
