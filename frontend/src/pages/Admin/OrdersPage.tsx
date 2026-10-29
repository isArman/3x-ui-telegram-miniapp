import React, { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { apiService } from '../../services/api'
import { Loading, Card, Button, Input } from '../../components/ui'
import { StatusBadge } from '../../components/ui/StatusBadge'
import { useNavigate } from 'react-router-dom'
import { Order } from '../../types'

export const AdminOrdersPage: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState('')
  const navigate = useNavigate()

  const { data: orders, isLoading, isError } = useQuery({
    queryKey: ['orders'],
    queryFn: () => apiService.getOrders(),
  })

  const filteredOrders = orders?.filter(order =>
    order.id.toString().includes(searchTerm) ||
    order.planSnapshotName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    order.status.toLowerCase().includes(searchTerm.toLowerCase())
  )

  if (isLoading) return <Loading message="Loading orders..." />
  if (isError) return <div className="p-4 text-center text-red-500">Error loading orders</div>

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-xl font-bold">All Orders</h1>
        <div className="w-64">
          <Input
            placeholder="Search orders..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      {filteredOrders && filteredOrders.length === 0 ? (
        <Card className="p-6 text-center">
          <p className="text-telegram-hint">No orders found</p>
        </Card>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {filteredOrders?.map((order) => (
            <Card
              key={order.id}
              className="p-4 cursor-pointer hover:bg-telegram-secondary-bg/50 transition-colors"
              onClick={() => navigate(`/order/${order.id}`)}
            >
              <div className="flex justify-between items-start mb-2">
                <h3 className="font-semibold">Order #{order.id}</h3>
                <StatusBadge status={order.status} />
              </div>
              <p className="text-sm text-telegram-hint mt-1">
                {order.planSnapshotName} · {order.planSnapshotDataGb} GB
              </p>
              <p className="text-xs text-telegram-hint mt-2">
                {new Date(order.createdAt).toLocaleString()}
              </p>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}
