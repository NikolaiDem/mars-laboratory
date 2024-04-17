"use client";
import { useAxios } from "@/context/axiosContext";
import { EnumReportStatus, IReport } from "@/types/types";
import { Table, TableProps, message } from "antd";
import { ColumnType } from "antd/es/table";
import React, { FC, useEffect } from "react";
import * as dayjs from "dayjs";
import { api } from "@/hooks/useRequest";
import { useState } from "react";

interface Props {}

const loacList = async () => {
  return await api.get("/report/list");
};

const ReportList: FC<Props> = () => {
  const [data, setData] = useState<IReport[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const [messageApi, contextHolder] = message.useMessage();

  useEffect(() => {
    (async () => {
      try {
        setLoading(true);
        const res = await api.get("/report/list", { withCredentials: true });
        console.log(res.data);
        // setData(res.data);
      } catch (error) {
        messageApi.open({
          type: "error",
          // @ts-ignore
          content: error.message,
        });
      }
    })();
  }, []);

  const column: ColumnType<IReport>[] = [
    {
      title: "Название отчета",
      dataIndex: "title",
      key: "title",
    },
    // {
    //   title: "Файл",
    //   dataIndex: "file",
    //   key: "title",
    //   render: (_, { file }) => file.name || file.fileName,
    // },
    {
      title: "Дата",
      dataIndex: "lastUpdated",
      key: "lastUpdated",
    },
  ];

  return (
    <>
      <Table dataSource={data || []} loading={loading} columns={column} />;
    </>
  );
};

export default ReportList;
