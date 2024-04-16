export interface IReport {
    title: string
			date: string,
			user: string,
			status: EnumReportStatus

}

export enum EnumReportStatus {
    CREATE = "CREATE",
    APPROVE=  "APPROVE",
    REJECT =  "REJECT",
    SEND = "SEND",
}