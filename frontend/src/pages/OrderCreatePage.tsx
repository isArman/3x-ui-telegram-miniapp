import React, { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { apiService } from '../services/api'
import { Loading, Card, Button, Input } from '../components/ui'
import { useNavigate, useParams } from 'react-router-dom'
import { CreateOrderRequest } from '../types'

export const OrderCreatePage: React.FC = () => {
  const { planId } = useParams<{ planId: string }>()
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  const { data: plan, isLoading: isLoadingPlan, isError: isErrorPlan } = useQuery({
    queryKey: ['plan', planId],
    queryFn: () => apiService.getPlans().then(plans => plans.find(p => p.id === parseInt(planId || '0'))),
    enabled: !!planId,
  })

  const [quantity, setQuantity] = useState(1)
  const [email, setEmail] = useState('')

  const createOrderMutation = useMutation({
    mutationFn: (data: CreateOrderRequest) => apiService.createOrder(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] })
      navigate('/orders')
    },
  })

  if (isLoadingPlan) return <Loading message="Loading plan..." />
  if (isErrorPlan || !plan) return <div className="p-4 text-center text-red-500">Plan not found</div>

  const handleCreateOrder = () => {
    if (!email) {
      return
    }
    createOrderMutation.mutate({ planId: plan.id })
  }

  return (
    <div className="space-y-4">
      <Card className="p-4">
        <h1 className="text-xl font-bold mb-4">Create Order</h1>
        <div className="space-y-3">
          <div>
            <h3 className="font-semibold">{plan.name}</h3>
            <p className="text-telegram-hint text-sm">
              {plan.dataAllowanceGb} GB · {plan.validityDurationDays} days
            </p>
            <p className="font-medium">{plan.priceToman.toLocaleString()} Toman</p>
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Email</label>
            <Input
              type="email"
              placeholder="your@email.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <Button
            onClick={handleCreateOrder}
            disabled={!email || createOrderMutation.isPending}
            loading={createOrderMutation.isPending}
          >
            Create Order
          </Button>
        </div>
      </Card>

      {createOrderMutation.isError && (
        <div className="p-4 text-center text-red-500">
          Failed to create order. Please try again.
        </div>
      )}
    </div>
  )
}