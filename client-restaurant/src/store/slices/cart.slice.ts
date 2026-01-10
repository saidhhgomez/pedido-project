// store/slices/cart.slice.ts
import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "..";

export interface CartItem {
  idProducto: number;
  nombre: string;
  cantidad: number;
  precioUnitario: number;
}

interface CartState {
  items: CartItem[];
}

const initialState: CartState = {
  items: [],
};

const cartSlice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    addItem: (state, action: PayloadAction<CartItem>) => {
      const existing = state.items.find(i => i.idProducto === action.payload.idProducto);
      if (existing) {
        existing.cantidad += action.payload.cantidad;
      } else {
        state.items.push(action.payload);
      }
    },
    decreaseItem: (state, action: PayloadAction<number>) => {
      const item = state.items.find(i => i.idProducto === action.payload);
      if (item) {
        item.cantidad -= 1;
        if (item.cantidad <= 0) {
          state.items = state.items.filter(i => i.idProducto !== action.payload);
        }
      }
    },
    removeItem: (state, action: PayloadAction<number>) => {
      state.items = state.items.filter(i => i.idProducto !== action.payload);
    },
    clearCart: (state) => {
      state.items = [];
    },
  },
});

export const { addItem, decreaseItem, removeItem, clearCart } = cartSlice.actions;
export const selectCart = (state: RootState) => state.cart.items;
export default cartSlice.reducer;
