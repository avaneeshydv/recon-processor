package com.recon.reconprocessor.service;

public enum ControlCharacterEnum {
  BACKSLASH("\\"), DOLLAR("$"), POW("^"), DOT("."), PIPE("|");

  public final String value;

  ControlCharacterEnum(String value) {
    this.value = value;
  }

}

