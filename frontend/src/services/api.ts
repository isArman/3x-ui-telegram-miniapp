import { Plan, Order, CreateOrderRequest } from '../types'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || ''

// Development flag - set to true to use mock data instead of real API
export const USE_MOCK_DATA = import.meta.env.VITE_USE_MOCK_DATA === 'true'

const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms))

class ApiService {
  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`
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
      return [...mockPlans]
    }
    return this.request<Plan[]>('/plans')
  }

  async createOrder(data: CreateOrderRequest): Promise<Order> {
    if (USE_MOCK_DATA) {
      await delay(800)
      const plan = mockPlans.find((p) => p.id === data.planId)
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
      return [...mockOrders]
    }
    return this.request<Order[]>('/orders')
  }

  async healthCheck(): Promise<{ status: string }> {
    return this.request<{ status: string }>('/health')
  }
}

export const apiService = new ApiService()

// Mock data import (only used when USE_MOCK_DATA is true)
import { mockPlans, mockOrders } from '../mocks/data'
