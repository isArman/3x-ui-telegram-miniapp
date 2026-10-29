import React from 'react'

export const Loading: React.FC<{ message?: string }> = ({ message = 'Loading...' }) => (
  <div className="flex flex-col items-center justify-center py-12 gap-3">
    <div className="w-8 h-8 border-2 border-telegram-link border-t-transparent rounded-full animate-spin" />
    <p className="text-sm text-telegram-hint">{message}</p>
  </div>
)
