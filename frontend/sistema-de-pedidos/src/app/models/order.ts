import { Status } from "./status.enum";

export interface Order {
  id?: any,
  status: Status,
  totalPrice: number,
  created: Date,
  client: {id: number},
  clientId: any,
}
