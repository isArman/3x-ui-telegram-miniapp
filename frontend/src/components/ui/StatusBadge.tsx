import React from 'react'
import { OrderStatus } from '../../types'

const statusColors: Record<OrderStatus, string> = {
  CREATED: 'bg-yellow-100 text-yellow-800',
  PENDING_PAYMENT: 'bg-blue-100 text-blue-800',
  PAYMENT_APPROVED: 'bg-green-100 text-green-800',
  PROVISIONING_PENDING: 'bg-purple-100 text-purple-800',
  PROVISIONED: 'bg-green-100 text-green-800',
  PROVISIONING_FAILED: 'bg-red-100 text-red-800',
  REJECTED: 'bg-red-100 text-red-800',
}

const statusLabels: Record<OrderStatus, string> = {
  CREATED: 'Created',
  PENDING_PAYMENT: 'Pending Payment',
  PAYMENT_APPROVED: 'Payment Approved',
  PROVISIONING_PENDING: 'Provisioning Pending',
  PROVISIONED: 'Provisioned',
  PROVISIONING_FAILED: 'Provisioning Failed',
  REJECTED: 'Rejected',
}

export const StatusBadge: React.FC<{ status: OrderStatus }> = ({ status }) => {
  return (
    <span className={`px-2 py-1 rounded-full text-xs font-medium ${statusColors[status]}`}>
      {statusLabels[status]}
    </span>
  )
}
