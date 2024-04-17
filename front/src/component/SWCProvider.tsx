// my-provider.tsx
"use client";
import React, { FC, ReactNode } from "react";
import { SWRConfig } from "swr";
import axios from "axios";

interface Props {
  children: ReactNode;
}
const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_CLIENT_URL;

const api = axios.create({
  baseURL: BASE_URL,
});
const SWCProvider: FC<Props> = ({ children }) => {
  return (
    <SWRConfig
      value={{
        refreshInterval: 3000,
        fetcher: (resource, init) => {
          console.log({ resource, init });
          return fetch(resource, init).then((res) => res.json());
        },
      }}
    >
      {children}
    </SWRConfig>
  );
};

export default SWCProvider;
