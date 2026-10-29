import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { apiService } from '../services/api'
import { Plan } from '../types'
import { useTheme } from '../components/ui/ThemeProvider'
import { Card, Loading, Button } from '../ui'
import { useNavigate } from 'react-router-dom'

export const HomePage: React.FC = () => {
  const { theme } = useTheme()
  const navigate = useNavigate()

  const { data: plans, isLoading, isError, error } = useQuery({
    queryKey: ['plans'],
    queryFn: () => apiService.getPlans(),
    staleTime: 5 * 60 * 1000,
  })

  if (isLoading) {
    return <Loading message="Loading plans..." />
  }

  if (isError) {
    return (
      <div className="p-6 text-center">
        <p className="text-red-500">Error loading plans</p>
        <Button onClick={() => navigate('/')">Retry</Button>
      </div>
    )
  )

  return (
    <div className="min-h-screen bg-telegram-bg text-telegram-text p-4 safe-area-top">
      <header className="mb-6">
        <h1 className="text-2xl font-bold mb-2">
          {theme === 'dark' ? 'Telegram Mini App' : 'Telegram Mini App'}
        </h1>
        <p className="text-telegram-hint">Subscription Management</p>
      </header>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {plans?.map((plan) => (
          <Card key={plan.id} className="p-4">
            <h3 className="font-semibold mb-2">{plan.name}</h3>
            <p className="text-telegram-hint mb-2">
              {plan.dataAllowanceGb} GB · {plan.validityDurationDays} days
            </p>
            <p className="font-medium">{plan.priceToman} Toman</p>
            <Button
              onClick={() => navigate(`/order/${plan.id}`)}
              className="mt-3"
            >
              Subscribe
            </Button>
          </Card>
        ))}
      </div>

      {isLoading || isError && <p className="mt-4 text-telegram-hint">No plans available</p>}
    </div>
  )
}