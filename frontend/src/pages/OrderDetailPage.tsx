import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { apiService } from '../services/api'
import { Loading, Card, Button } from '../components/ui'
import { useNavigate, useLocation } from 'react-router-dom'
import { Order, OrderStatus } from '../types'

export const OrderDetailPage: React.FC = () => {
  const { orderId } = useLocation()
  const navigate = useNavigate()

  const { data: orders, isLoading, isError } = useQuery({
    queryKey: ['orders'],
    queryFn: () => apiService.getOrders(),
  })

  const order = orders?.find(o => o.id === parseInt(orderId?.toString() || '0'))

  if (isLoading) return <Loading message="Loading order..." />
  if (isError || !order) return <div className="p-4 text-center text-red-500">Order not found</div>

  const statusColors: Record<OrderStatus, string> = {
    CREATED: 'bg-yellow-100 text-yellow-800',
    PENDING_PAYMENT: 'bg-blue-100 text-blue-800',
    PAYMENT_APPROVED: 'bg-green-100 text-green-800',
    PROVISIONING_PENDING: 'bg-purple-100 text-purple-800',
    PROVISIONED: 'bg-green-100 text-green-800',
    PROVISIONING_FAILED: 'bg-red-100 text-red-800',
    REJECTED: 'bg-red-100 text-red-800',
  }

  return (
    <div className="space-y-4">
      <Card className="p-4">
        <h1 className="text-xl font-bold mb-4">Order #{order.id}</h1>
        <div className="space-y-3">
          <div>
            <p className="text-telegram-hint text-sm">Plan</p>
            <p className="font-medium">{order.planSnapshotName}</p>
          </div>
          <div>
            <p className="text-telegram-hint text-sm">Data Allowance</p>
            <p className="font-medium">{order.planSnapshotDataGb} GB</p>
          </div>
          <div>
            <p className="text-telegram-hint text-sm">Validity</p>
            <p className="font-medium">{order.planSnapshotDurationDays} days</p>
          </div>
          <div>
            <p className="text-telegram-hint text-sm">Price</p>
            <p className="font-medium">{order.planSnapshotPriceToman.toLocaleString()} Toman</p>
          </div>
          <div>
            <p className="text-telegram-hint text-sm">Status</p>
            <span className={`px-2 py-1 rounded-full text-xs font-medium ${statusColors[order.status]}`}>
              {order.status}
            </span>
          </div>
          <div>
            <p className="text-telegram-hint text-sm">Created</p>
            <p className="font-medium">{new Date(order.createdAt).toLocaleString()}</p>
          </div>
        </div>
      </Card>
    </div>
  )
}