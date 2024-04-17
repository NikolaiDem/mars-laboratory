"use client";
import { useAxios } from "@/context/axiosContext";
import { EnumReportStatus, IReport } from "@/types/types";
import { Table, TableProps } from "antd";
import { ColumnType } from "antd/es/table";
import React, { FC, useEffect } from "react";
import * as dayjs from "dayjs";

interface Props {}

const ReportList: FC<Props> = () => {
  const { data, loaded } = useAxios<IReport[]>("/report/list");
  useEffect(() => {
    console.log(data);
  }, [data]);

  const column: ColumnType<IReport>[] = [
    {
      title: "Название отчета",
      dataIndex: "title",
      key: "title",
    },
    {
      title: "Дата",
      dataIndex: "lastUpdated",
      key: "lastUpdated",
    },
  ];
  return <Table dataSource={data || []} loading={!loaded} columns={column} />;
};

export default ReportList;
