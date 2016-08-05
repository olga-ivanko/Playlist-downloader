/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.codefire;

import java.net.URL;

/**
 *
 * @author homefulloflove
 */
public class DownloadFile {

    private URL address;
    private State state = State.WAIT;

    public DownloadFile(URL address) {
        this.address = address;
    }

    public URL getAddress() {
        return address;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", state.getShortName(), address);
    }

    public enum State {
        WAIT("W"), READY("R"), PROGRESS("D"), COMPLETE("C");

        private String shortName;

        private State(String shortName) {
            this.shortName = shortName;
        }

        public String getShortName() {
            return shortName;
        }

    }

}
