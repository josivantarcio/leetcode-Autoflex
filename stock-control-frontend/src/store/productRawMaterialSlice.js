import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import api from '../api/axios';

export const fetchProductRawMaterials = createAsyncThunk('productRawMaterials/fetchAll', async () => {
  const res = await api.get('/product-raw-materials');
  return res.data;
});

export const createProductRawMaterial = createAsyncThunk('productRawMaterials/create', async (assoc) => {
  const res = await api.post('/product-raw-materials', assoc);
  return res.data;
});

export const updateProductRawMaterial = createAsyncThunk('productRawMaterials/update', async (assoc) => {
  const res = await api.put(`/product-raw-materials/${assoc.id}`, assoc);
  return res.data;
});

export const deleteProductRawMaterial = createAsyncThunk('productRawMaterials/delete', async (id) => {
  await api.delete(`/product-raw-materials/${id}`);
  return id;
});

const productRawMaterialSlice = createSlice({
  name: 'productRawMaterials',
  initialState: { items: [], status: 'idle', error: null },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchProductRawMaterials.pending, (state) => { state.status = 'loading'; })
      .addCase(fetchProductRawMaterials.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.items = action.payload;
      })
      .addCase(fetchProductRawMaterials.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(createProductRawMaterial.fulfilled, (state, action) => {
        state.items.push(action.payload);
      })
      .addCase(updateProductRawMaterial.fulfilled, (state, action) => {
        const idx = state.items.findIndex((a) => a.id === action.payload.id);
        if (idx !== -1) state.items[idx] = action.payload;
      })
      .addCase(deleteProductRawMaterial.fulfilled, (state, action) => {
        state.items = state.items.filter((a) => a.id !== action.payload);
      });
  },
});

export default productRawMaterialSlice.reducer;
