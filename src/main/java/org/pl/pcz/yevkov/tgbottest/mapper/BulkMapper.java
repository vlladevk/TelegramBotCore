package org.pl.pcz.yevkov.tgbottest.mapper;

import java.util.List;

public interface BulkMapper<From, To> {
    List<To> mapAll(From input);
}