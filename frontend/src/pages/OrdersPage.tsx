import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { apiService } from '../services/api'
import { Loading, Card } from '../components/ui'
import { StatusBadge } from '../components/ui/StatusBadge'
import { useNavigate } from 'react-router-dom'

export const OrdersPage: React.FC = () => {
  const navigate = useNavigate()

  const { data: orders, isLoading, isError } = useQuery({
    queryKey: ['orders'],
    queryFn: () => apiService.getOrders(),
  })

  if (isLoading) return <Loading message="Loading orders..." />
  if (isError) return <div className="p-4 text-center text-red-500">Error loading orders</div>

  return (
    <div className="space-y-4">
      <h1 className="text-xl font-bold mb-4">Your Orders</h1>
      {orders && orders.length === 0 ? (
        <Card className="p-6 text-center">
          <p className="text-telegram-hint">No orders found</p>
          <button
            onClick={() => navigate('/plans')}
            className="mt-3 text-telegram-link text-sm font-medium"
          >
            Browse plans
          </button>
        </Card>
      ) : (
        orders?.map((order) => (
          <Card
            key={order.id}
            className="p-4 cursor-pointer"
            onClick={() => navigate(`/order/${order.id}`)}
          >
            <div className="flex items-center justify-between">
              <div>
                <h3 className="font-semibold">Order #{order.id}</h3>
                <p className="text-telegram-hint text-sm mt-1">
                  {order.planSnapshotName} · {order.planSnapshotDataGb} GB
                </p>
              </div>
              <StatusBadge status={order.status} />
            </div>
            <p className="text-xs text-telegram-hint mt-2">
              {new Date(order.createdAt).toLocaleString()}
            </p>
          </Card>
        ))
      )}
    </div>
  )
}
