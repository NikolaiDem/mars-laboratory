"use client";

import axios, {
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  CreateAxiosDefaults,
  InternalAxiosRequestConfig,
  Method,
} from "axios";
import {
  ReactNode,
  createContext,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState,
} from "react";

const AxiosContext = createContext<AxiosInstance | null>(null);
const AxiosInstanceProvider = ({
  config = {},
  requestInterceptors = [],
  responseInterceptors = [],
  children,
}: {
  config: CreateAxiosDefaults;
  requestInterceptors?: ((
    config: InternalAxiosRequestConfig,
  ) => InternalAxiosRequestConfig | Promise<InternalAxiosRequestConfig>)[];
  responseInterceptors?: ((response: AxiosResponse) => AxiosResponse)[];
  children: ReactNode;
}) => {
  const instanceRef = useRef(axios.create(config));

  useEffect(() => {
    requestInterceptors.forEach((interceptor) => {
      instanceRef.current.interceptors.request.use(interceptor);
    });
    responseInterceptors.forEach((interceptor) => {
      instanceRef.current.interceptors.response.use(interceptor);
    });
  }, []);

  return (
    <AxiosContext.Provider value={instanceRef.current}>
      {children}
    </AxiosContext.Provider>
  );
};

export function useAxios<
  IResponseRes extends {} = {},
  IDataReqBbody extends {} = {},
>(
  url: string,
  payload?: AxiosRequestConfig<IDataReqBbody>["data"],
  method?: Method,
) {
  const [data, setData] = useState<IResponseRes | null>(null);
  const [error, setError] = useState<unknown>("");
  const [loaded, setLoaded] = useState(false);
  const contextInstance = useContext(AxiosContext);
  const instance = useMemo(() => {
    return contextInstance || axios;
  }, [contextInstance]);
  const controllerRef = useRef(new AbortController());
  const cancel = () => {
    controllerRef.current.abort();
  };

  useEffect(() => {
    (async () => {
      try {
        const response = await instance.request({
          signal: controllerRef.current.signal,
          data: payload,
          method: method || "get",
          withCredentials: true,
          url,
        });

        setData(response.data as IResponseRes);
      } catch (error) {
        setError(error);
      } finally {
        setLoaded(true);
      }
    })();
  }, []);

  return { cancel, data, error, loaded };
}

export default AxiosInstanceProvider;
