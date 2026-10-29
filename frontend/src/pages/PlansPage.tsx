import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { apiService } from '../services/api'
import { Loading, Card, Button } from '../components/ui'
import { useNavigate } from 'react-router-dom'

export const PlansPage: React.FC = () => {
  const navigate = useNavigate()

  const { data: plans, isLoading, isError } = useQuery({
    queryKey: ['plans'],
    queryFn: () => apiService.getPlans(),
  })

  if (isLoading) return <Loading message="Loading plans..." />
  if (isError) return <div className="p-4 text-center text-red-500">Error loading plans</div>

  return (
    <div className="space-y-4">
      <h1 className="text-xl font-bold mb-4">Available Plans</h1>
      {plans?.map((plan) => (
        <Card key={plan.id} className="p-4">
          <h3 className="font-semibold text-lg">{plan.name}</h3>
          <p className="text-telegram-hint mt-1">
            {plan.dataAllowanceGb} GB data · {plan.validityDurationDays} days validity
          </p>
          <p className="font-medium mt-2">{plan.priceToman.toLocaleString()} Toman</p>
          <Button
            onClick={() => navigate(`/order/${plan.id}`)}
            className="mt-3 w-full"
            size="sm"
          >
            Select Plan
          </Button>
        </Card>
      ))}
    </div>
  )
}
