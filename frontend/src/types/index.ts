// API Types - mirrors backend entities

export interface Plan {
  id: number
  name: string
  dataAllowanceGb: number
  validityDurationDays: number
  priceToman: number
}

export type OrderStatus =
  | 'CREATED'
  | 'PENDING_PAYMENT'
  | 'PAYMENT_APPROVED'
  | 'PROVISIONING_PENDING'
  | 'PROVISIONED'
  | 'PROVISIONING_FAILED'
  | 'REJECTED'

export interface Order {
  id: number
  telegramUserId: number
  planId: number
  planSnapshotName: string
  planSnapshotDataGb: number
  planSnapshotDurationDays: number
  planSnapshotPriceToman: number
  status: OrderStatus
  createdAt: string
}

export interface PaymentAudit {
  id: number
  orderId: number
  adminTelegramUserId: number
  action: string
  notes: string
  createdAt: string
}

export interface CreateOrderRequest {
  planId: number
}

export interface ApprovePaymentRequest {
  orderId: number
}

export interface RejectPaymentRequest {
  orderId: number
  reason?: string
}

export interface CreatePlanRequest {
  name: string
  dataAllowanceGb: number
  validityDurationDays: number
  priceToman: number
}

export interface HealthStatus {
  status: string
}
