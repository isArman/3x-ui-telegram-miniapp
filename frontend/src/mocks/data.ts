import { Plan, Order } from '../types'

export const mockPlans: Plan[] = [
  { id: 1, name: 'Basic', dataAllowanceGb: 10, validityDurationDays: 30, priceToman: 50000 },
  { id: 2, name: 'Standard', dataAllowanceGb: 50, validityDurationDays: 30, priceToman: 150000 },
  { id: 3, name: 'Premium', dataAllowanceGb: 100, validityDurationDays: 30, priceToman: 250000 },
]

export const mockOrders: Order[] = [
  {
    id: 101,
    telegramUserId: 123456,
    planId: 1,
    planSnapshotName: 'Basic',
    planSnapshotDataGb: 10,
    planSnapshotDurationDays: 30,
    planSnapshotPriceToman: 50000,
    status: 'CREATED',
    createdAt: '2026-10-29T10:00:00Z',
  },
  {
    id: 102,
    telegramUserId: 123456,
    planId: 2,
    planSnapshotName: 'Standard',
    planSnapshotDataGb: 50,
    planSnapshotDurationDays: 30,
    planSnapshotPriceToman: 150000,
    status: 'PAYMENT_APPROVED',
    createdAt: '2026-10-28T14:30:00Z',
  },
]
