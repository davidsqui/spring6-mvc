package com.dasc.spring6mvc.services;

import com.dasc.spring6mvc.model.BeerCSVRecord;
import java.io.File;
import java.util.List;

public interface BeerCsvService {

  List<BeerCSVRecord> convertCSV(File csvFile);

}
