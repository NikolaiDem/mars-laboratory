"use client";
import ReportFrom from "@/component/ReportFrom";
import { useAxios } from "@/context/axiosContext";
import { Flex } from "antd";
import Layout, { Content } from "antd/es/layout/layout";
import { NextPage } from "next";

interface Props {}

const Page: NextPage<Props> = ({}) => {
  return (
    <Layout>
      <Flex align="center" justify="center">
        <ReportFrom onSubmit={(report) => {}} type="create" />
      </Flex>
    </Layout>
  );
};

export default Page;
