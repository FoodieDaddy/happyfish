package com.mdmd.util.weixinbutton;

import java.util.List;

public class CommonButton extends Button {
    private List<Button> sub_button;

    public List<Button> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<Button> sub_button) {
        this.sub_button = sub_button;
    }
}