import React, { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { apiService } from '../../services/api'
import { Loading, Card, Button, Input } from '../../components/ui'
import { useNavigate } from 'react-router-dom'
import { Plan } from '../../types'

export const AdminPlansPage: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState('')
  const navigate = useNavigate()

  const { data: plans, isLoading, isError } = useQuery({
    queryKey: ['plans'],
    queryFn: () => apiService.getPlans(),
  })

  const filteredPlans = plans?.filter(plan =>
    plan.name.toLowerCase().includes(searchTerm.toLowerCase())
  )

  if (isLoading) return <Loading message="Loading plans..." />
  if (isError) return <div className="p-4 text-center text-red-500">Error loading plans</div>

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-xl font-bold">All Plans</h1>
        <div className="w-64">
          <Input
            placeholder="Search plans..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      {filteredPlans?.length === 0 ? (
        <Card className="p-6 text-center">
          <p className="text-telegram-hint">No plans found</p>
        </Card>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {filteredPlans?.map((plan) => (
            <Card
              key={plan.id}
              className="p-4 cursor-pointer hover:bg-telegram-secondary-bg/50 transition-colors"
              onClick={() => navigate(`/admin/plans/${plan.id}`)}
            >
              <h3 className="font-semibold mb-2">{plan.name}</h3>
              <p className="text-telegram-hint mb-1">
                {plan.dataAllowanceGb} GB · {plan.validityDurationDays} days
              </p>
              <p className="font-medium mt-2">{plan.priceToman.toLocaleString()} Toman</p>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}
