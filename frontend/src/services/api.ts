import { Plan, Order, PaymentAudit, OrderStatus, CreateOrderRequest, ApprovePaymentRequest, RejectPaymentRequest, CreatePlanRequest, HealthStatus } from '../types'

export const USE_MOCK_DATA = import.meta.env.VITE_USE_MOCK_DATA === 'true'

const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms))

class ApiService {
  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${import.meta.env.VITE_API_BASE_URL}${endpoint}`
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      ...options.headers,
    }

    const response = await fetch(url, {
      ...options,
      headers,
    })

    if (!response.ok) {
      throw new Error(`API Error: ${response.status} ${response.statusText}`)
    }

    return response.json()
  }

  async getPlans(): Promise<Plan[]> {
    if (USE_MOCK_DATA) {
      await delay(500)
      return [
        { id: 1, name: 'Basic', dataAllowanceGb: 10, validityDurationDays: 30, priceToman: 50000 },
        { id: 2, name: 'Standard', dataAllowanceGb: 50, validityDurationDays: 30, priceToman: 150000 },
        { id: 3, name: 'Premium', dataAllowanceGb: 100, validityDurationDays: 30, priceToman: 250000 },
      ]
    }
    return this.request<Plan[]>('/plans')
  }

  async createOrder(data: CreateOrderRequest): Promise<Order> {
    if (USE_MOCK_DATA) {
      await delay(800)
      const plan = [
        { id: 1, name: 'Basic', dataAllowanceGb: 10, validityDurationDays: 30, priceToman: 50000 },
        { id: 2, name: 'Standard', dataAllowanceGb: 50, validityDurationDays: 30, priceToman: 150000 },
        { id: 3, name: 'Premium', dataAllowanceGb: 100, validityDurationDays: 30, priceToman: 250000 },
      ].find((p) => p.id === data.planId)
      if (!plan) {
        throw new Error('Plan not found')
      }
      return {
        id: Date.now(),
        telegramUserId: 123456,
        planId: plan.id,
        planSnapshotName: plan.name,
        planSnapshotDataGb: plan.dataAllowanceGb,
        planSnapshotDurationDays: plan.validityDurationDays,
        planSnapshotPriceToman: plan.priceToman,
        status: 'CREATED',
        createdAt: new Date().toISOString(),
      }
    }
    return this.request<Order>('/orders', {
      method: 'POST',
      body: JSON.stringify(data),
    })
  }

  async getOrders(): Promise<Order[]> {
    if (USE_MOCK_DATA) {
      await delay(500)
      return [
        {
          id: 101,
          telegramUserId: 123456,
          planId: 1,
          planSnapshotName: 'Basic',
          planSnapshotDataGb: 10,
          planSnapshotDurationDays: 30,
          planSnapshotPriceToman: 50000,
          status: 'CREATED',
          createdAt: '2026-10-29T10:00:00Z',
        },
        {
          id: 102,
          telegramUserId: 123456,
          planId: 2,
          planSnapshotName: 'Standard',
          planSnapshotDataGb: 50,
          planSnapshotDurationDays: 30,
          planSnapshotPriceToman: 150000,
          status: 'PAYMENT_APPROVED',
          createdAt: '2026-10-28T14:30:00Z',
        },
      ]
    }
    return this.request<Order[]>('/orders')
  }

  async healthCheck(): Promise<{ status: string }> {
    return this.request<{ status: string }>('/health')
  }

  async approvePayment(data: ApprovePaymentRequest): Promise<Order> {
    if (USE_MOCK_DATA) {
      await delay(800)
      const order = [
        {
          id: 101,
          telegramUserId: 123456,
          planId: 1,
          planSnapshotName: 'Basic',
          planSnapshotDataGb: 10,
          planSnapshotDurationDays: 30,
          planSnapshotPriceToman: 50000,
          status: 'CREATED',
          createdAt: '2026-10-29T10:00:00Z',
        },
        {
          id: 102,
          telegramUserId: 123456,
          planId: 2,
          planSnapshotName: 'Standard',
          planSnapshotDataGb: 50,
          planSnapshotDurationDays: 30,
          planSnapshotPriceToman: 150000,
          status: 'PAYMENT_APPROVED',
          createdAt: '2026-10-28T14:30:00Z',
        },
      ].find((o) => o.id === data.orderId)
      if (!order) {
        throw new Error('Order not found')
      }
      order.status = 'PAYMENT_APPROVED'
      return order
    }
    return this.request<Order>('/admin/payments/approve', {
      method: 'POST',
      body: JSON.stringify(data),
    })
  }

  async rejectPayment(data: RejectPaymentRequest): Promise<Order> {
    if (USE_MOCK_DATA) {
      await delay(800)
      const order = [
        {
          id: 101,
          telegramUserId: 123456,
          planId: 1,
          planSnapshotName: 'Basic',
          planSnapshotDataGb: 10,
          planSnapshotDurationDays: 30,
          planSnapshotPriceToman: 50000,
          status: 'CREATED',
          createdAt: '2026-10-29T10:00:00Z',
        },
        {
          id: 102,
          telegramUserId: 123456,
          planId: 2,
          planSnapshotName: 'Standard',
          planSnapshotDataGb: 50,
          planSnapshotDurationDays: 30,
          planSnapshotPriceToman: 150000,
          status: 'PAYMENT_APPROVED',
          createdAt: '2026-10-28T14:30:00Z',
        },
      ].find((o) => o.id === data.orderId)
      if (!order) {
        throw new Error('Order not found')
      }
      order.status = 'REJECTED'
      return order
    }
    return this.request<Order>('/admin/payments/reject', {
      method: 'POST',
      body: JSON.stringify(data),
    })
  }
}

export const apiService = new ApiService()

// Mock data imports (only used when USE_MOCK_DATA is true)
import { mockPlans, mockOrders } from '../mocks/data'
import { mockPlans as _mockPlans } from '../mocks/data'