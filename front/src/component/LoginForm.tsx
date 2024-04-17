"use client";
import { useAxios } from "@/context/axiosContext";
import { api } from "@/hooks/useRequest";
import { LockOutlined, UserOutlined } from "@ant-design/icons";
import { Button, Flex, Space } from "antd";
import Checkbox from "antd/es/checkbox/Checkbox";
import Form from "antd/es/form";
import Input from "antd/lib/input/Input";
import Title from "antd/lib/typography/Title";
import axios from "axios";

const LoginForm = () => {
  const onFinish = async (val: any) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const formdata = new FormData();
    Object.entries(val).map((item) => {
      formdata.append(item[0], item[1] as string);
    });
    const res = await api.post("/login", formdata, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    console.log({ res });
    console.log("Received values of form: ");
  };

  return (
    <Flex className="w-full " align="center" justify="center">
      <Space direction="vertical">
        <Title>Вход</Title>
        <Form
          name="normal_login"
          className="login-form"
          initialValues={{
            remember: true,
          }}
          onFinish={onFinish}
        >
          <Form.Item
            name="username"
            rules={[
              {
                required: true,
                message: "Please input your Username!",
              },
            ]}
          >
            <Input
              prefix={<UserOutlined className="site-form-item-icon" />}
              placeholder="Username"
            />
          </Form.Item>
          <Form.Item
            name="password"
            rules={[
              {
                required: true,
                message: "Please input your Password!",
              },
            ]}
          >
            <Input
              prefix={<LockOutlined className="site-form-item-icon" />}
              type="password"
              placeholder="Password"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="login-form-button"
            >
              Войти
            </Button>
          </Form.Item>
        </Form>
      </Space>
    </Flex>
  );
};

export default LoginForm;
