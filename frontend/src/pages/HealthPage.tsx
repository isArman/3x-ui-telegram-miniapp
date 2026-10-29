import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { apiService } from '../services/api'
import { Loading } from '../components/ui'

export const HealthPage: React.FC = () => {
  const { data, isLoading, isError } = useQuery({
    queryKey: ['health'],
    queryFn: () => apiService.healthCheck(),
    retry: false,
  })

  if (isLoading) return <Loading message="Checking health..." />
  if (isError) return <div className="p-4 text-center text-red-500">Backend unavailable</div>

  return (
    <div className="p-6 text-center">
      <h1 className="text-xl font-bold mb-4">Health Status</h1>
      <p className="text-2xl font-bold text-green-500">{data?.status ?? 'UNKNOWN'}</p>
    </div>
  )
}
