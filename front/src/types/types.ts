export interface IReport {
  id: string;
  title: string;
  lastUpdated: string;
  author: { id: string; name: string };
  state: EnumReportStatus;
  file: { name: string; fileName: string };
  comment: string;
}

export interface IUser {
  name: string;
}

export enum EnumReportStatus {
  CREATE = "CREATE",
  APPROVE = "APPROVE",
  REJECT = "REJECT",
  SEND = "SEND",
}

export type IReortForm = Omit<IReport, "">;
