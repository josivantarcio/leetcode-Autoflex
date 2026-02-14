import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import api from '../api/axios';

export const fetchRawMaterials = createAsyncThunk('rawMaterials/fetchAll', async () => {
  const res = await api.get('/raw-materials');
  return res.data;
});

export const createRawMaterial = createAsyncThunk('rawMaterials/create', async (material) => {
  const res = await api.post('/raw-materials', material);
  return res.data;
});

export const updateRawMaterial = createAsyncThunk('rawMaterials/update', async (material) => {
  const res = await api.put(`/raw-materials/${material.id}`, material);
  return res.data;
});

export const deleteRawMaterial = createAsyncThunk('rawMaterials/delete', async (id) => {
  await api.delete(`/raw-materials/${id}`);
  return id;
});

const rawMaterialSlice = createSlice({
  name: 'rawMaterials',
  initialState: { items: [], status: 'idle', error: null },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchRawMaterials.pending, (state) => { state.status = 'loading'; })
      .addCase(fetchRawMaterials.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.items = action.payload;
      })
      .addCase(fetchRawMaterials.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      })
      .addCase(createRawMaterial.fulfilled, (state, action) => {
        state.items.push(action.payload);
      })
      .addCase(updateRawMaterial.fulfilled, (state, action) => {
        const idx = state.items.findIndex((m) => m.id === action.payload.id);
        if (idx !== -1) state.items[idx] = action.payload;
      })
      .addCase(deleteRawMaterial.fulfilled, (state, action) => {
        state.items = state.items.filter((m) => m.id !== action.payload);
      });
  },
});

export default rawMaterialSlice.reducer;
