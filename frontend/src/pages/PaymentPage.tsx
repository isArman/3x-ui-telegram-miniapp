import React, { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { apiService } from '../services/api'
import { Loading, Card, Button, Input } from '../components/ui'
import { StatusBadge } from '../components/ui/StatusBadge'
import { useNavigate, useParams } from 'react-router-dom'
import { Order } from '../types'

export const PaymentPage: React.FC = () => {
  const { orderId } = useParams<{ orderId: string }>()
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  const { data: orders, isLoading: isLoadingOrders, isError: isErrorOrders } = useQuery({
    queryKey: ['orders'],
    queryFn: () => apiService.getOrders(),
    enabled: !!orderId,
  })

  const order = orders?.find(o => o.id === parseInt(orderId || '0'))

  const [receiptFile, setReceiptFile] = useState<File | null>(null)

  const uploadReceiptMutation = useMutation({
    mutationFn: async (file: File) => {
      const formData = new FormData()
      formData.append('file', file)
      const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/orders/${orderId}/receipt`, {
        method: 'POST',
        body: formData,
      })
      if (!response.ok) throw new Error('Upload failed')
      return response.json()
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] })
      navigate(`/order/${orderId}`)
    },
  })

  if (isLoadingOrders) return <Loading message="Loading order..." />
  if (isErrorOrders || !order) return <div className="p-4 text-center text-red-500">Order not found</div>

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setReceiptFile(e.target.files[0])
    }
  }

  const handleUpload = () => {
    if (receiptFile) {
      uploadReceiptMutation.mutate(receiptFile)
    }
  }

  return (
    <div className="space-y-4">
      <Card className="p-4">
        <h1 className="text-xl font-bold mb-4">Payment for Order #{order.id}</h1>
        <div className="space-y-3">
          <div>
            <p className="text-telegram-hint text-sm">Plan</p>
            <p className="font-medium">{order.planSnapshotName}</p>
          </div>
          <div>
            <p className="text-telegram-hint text-sm">Amount</p>
            <p className="font-medium text-lg">{order.planSnapshotPriceToman.toLocaleString()} Toman</p>
          </div>
          <div>
            <p className="text-telegram-hint text-sm">Status</p>
            <StatusBadge status={order.status} />
          </div>

          {order.status === 'CREATED' || order.status === 'PENDING_PAYMENT' ? (
            <div className="space-y-3 pt-4 border-t border-telegram-text/10">
              <p className="text-sm text-telegram-hint">
                Please transfer the exact amount to the bank card below and upload a clear image of the receipt.
              </p>
              <div className="bg-telegram-bg p-3 rounded-lg border border-telegram-text/20">
                <p className="font-medium">Bank: {import.meta.env.VITE_PAYMENT_BANK_NAME || 'Bank Name'}</p>
                <p className="font-medium">Card: {import.meta.env.VITE_PAYMENT_CARD_NUMBER || '•••• •••• •••• ••••'}</p>
                <p className="font-medium">Holder: {import.meta.env.VITE_PAYMENT_ACCOUNT_HOLDER || 'Account Holder'}</p>
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">Upload Receipt</label>
                <Input
                  type="file"
                  accept="image/*"
                  onChange={handleFileChange}
                />
              </div>
              <Button
                onClick={handleUpload}
                disabled={!receiptFile || uploadReceiptMutation.isPending}
                loading={uploadReceiptMutation.isPending}
              >
                Submit Receipt
              </Button>
            </div>
          ) : (
            <div className="pt-4 border-t border-telegram-text/10">
              <p className="text-sm text-telegram-hint">
                This order is already {order.status.toLowerCase().replace('_', ' ')}.
              </p>
            </div>
          )}
        </div>
      </Card>

      {uploadReceiptMutation.isError && (
        <div className="p-4 text-center text-red-500">
          Failed to upload receipt. Please try again.
        </div>
      )}
    </div>
  )
}