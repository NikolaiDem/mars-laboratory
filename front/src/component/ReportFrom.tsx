"use client";
import { useAxios } from "@/context/axiosContext";
import useRequest from "@/hooks/useRequest";
import { IReport } from "@/types/types";
import { Button, Space, Upload } from "antd";
import Form, { useForm } from "antd/es/form/Form";
import FormItem from "antd/es/form/FormItem";
import Input from "antd/es/input/Input";
import Title from "antd/es/typography/Title";

import React, { FC } from "react";

interface Props {
  type: "edit" | "create";
  value?: IReport;
  onSubmit: (val: IReport) => void;
}

const ReportFrom: FC<Props> = ({ type }) => {
  const [form] = useForm();
  return (
    <>
      <Space>
        <Form>
          <FormItem label="Имя отчета">
            <Input placeholder="Имя отчета" />
          </FormItem>
          <FormItem label="Файл отчета">
            <Upload
              multiple={false}
              customRequest={(cal) => {
                console.log(cal);
              }}
              onChange={() => {}}
            >
              <Button>Загрузить файл отчета</Button>
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
