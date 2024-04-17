"use client";
import Table from "antd/es/table/Table";
import Layout, { Content, Header } from "antd/es/layout/layout";
import { NextPage } from "next";
import Title from "antd/es/typography/Title";
import ReportList from "../../component/ReportList";
import { Button, Flex, Modal, Space, Upload } from "antd";
import { useRouter } from "next/navigation";
import { ExclamationCircleFilled } from "@ant-design/icons";
import ReportFrom from "@/component/ReportFrom";

interface Props {}

const Page: NextPage<Props> = ({}) => {
  const router = useRouter();

  return (
    <Layout className="px-16 py-12">
      <Flex justify="space-between">
        <Space>
          <Title>Список отчетов</Title>
        </Space>
        <Space>
          <Button onClick={() => router.push("/reports/create")} type="primary">
            Создать отчет
          </Button>
          <Upload accept="json">
            <Button>Загрузить периодов</Button>
          </Upload>
        </Space>
      </Flex>
      <Content>
        <ReportList />
      </Content>
    </Layout>
  );
};

export default Page;
