"use client";
import { useAxios } from "@/context/axiosContext";
import useRequest from "@/hooks/useRequest";
import { IReport } from "@/types/types";
import { FileExclamationOutlined, FileOutlined } from "@ant-design/icons";
import { Button, Space, Upload } from "antd";
import Form, { useForm } from "antd/es/form/Form";
import FormItem from "antd/es/form/FormItem";
import Input from "antd/es/input/Input";
import Title from "antd/es/typography/Title";

import React, { FC } from "react";
import { useState } from "react";

interface Props {
  type: "edit" | "create";
  value?: IReport;
  onSubmit: (val: IReport) => void;
}

const ReportFrom: FC<Props> = ({ type }) => {
  const [name, setName] = useState<string>("");
  const [file, setFile] = useState<File>();

  return (
    <>
      <Space
        direction="vertical"
        className="w-full h-[100vh] flex justify-center items-center"
      >
        <Title>Создание отчета</Title>
        <Form>
          <FormItem label="Имя отчета">
            <Input
              value={name}
              onChange={({ target: { value } }) => setName(value)}
              placeholder="Имя отчета"
            />
          </FormItem>
          <FormItem label="Файл отчета">
            <Upload
              multiple={false}
              customRequest={({}) => {
                // console.log({ fi });
                // setFile()
              }}
              progress={{ showInfo: false }}
              fileList={[]}
              onChange={(info) => {
                console.log({ info });
                // setFile(info.file);
              }}
            >
              {file ? (
                <Space>
                  <FileOutlined />
                  {file.name}
                </Space>
              ) : (
                <Button>Прикрепить файл</Button>
              )}
            </Upload>
          </FormItem>
          <FormItem wrapperCol={{ offset: 6, span: 16 }}>
            <Button type="primary" htmlType="submit">
              Сохранить отчет
            </Button>
          </FormItem>
        </Form>
      </Space>
    </>
  );
};

export default ReportFrom;
