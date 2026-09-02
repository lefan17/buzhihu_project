package com.example.mapper;

import com.example.entity.Report;

import java.util.List;

public interface ReportMapper {

    void insert(Report report);

    List<Report> selectAll(Report report);

    Report selectById(Integer id);

    void updateById(Report report);
}
