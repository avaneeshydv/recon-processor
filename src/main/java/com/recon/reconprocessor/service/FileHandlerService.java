package com.recon.reconprocessor.service;

import java.io.InputStream;
import java.util.List;

public interface FileHandlerService {
  List<String> readFileData(InputStream inputStream, int sheet);

}
