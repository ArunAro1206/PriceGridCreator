import  { useEffect, useState } from 'react';

type PriceRow = {
  height: number;
  values: number[];
};

type GridData = {
  widths: number[];
  heights: number[];
  prices: PriceRow[];
  errorCode: number;
};

export default function App() {
  const [gridData, setGridData] = useState<GridData | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [showModal, setShowModal] = useState<boolean>(false);
  const [formData, setFormData] = useState<{ height: string; width: string; price: string }>({ height: '', width: '', price: '' });
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const [editedCells, setEditedCells] = useState<{ height: number; width: number; price: number }[]>([]);

  const fetchTheData = async () => {
    try {
      setLoading(true);
      const response = await fetch('http://localhost:8080/api/getPriceDetails');
      if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
      const data: GridData = await response.json();
      setGridData(data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTheData();
  }, []);

  const handleAdd = async () => {
    const { height, width, price } = formData;
    if (!height || !width || !price) {
      alert('Please fill all fields.');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/createPriceGrid', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          height: parseInt(height),
          width: parseInt(width),
          price: parseFloat(price),
        }),
      });

      if (!response.ok) throw new Error(`Error ${response.status}: Failed to create price entry`);

      setShowModal(false);
      setFormData({ height: '', width: '', price: '' });
      fetchTheData();
    } catch (err: any) {
      alert(`Failed to submit: ${err.message}`);
    }
  };

  const handleSaveChanges = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/updatePriceGrid', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(editedCells),
      });

      if (!response.ok) throw new Error(`Error ${response.status}: Failed to update`);

      setIsEditing(false);
      setEditedCells([]);
      fetchTheData();
    } catch (err: any) {
      alert('Update failed: ' + err.message);
    }
  };

  if (loading) return <p className="text-center mt-10">Loading...</p>;
  if (error) return <p className="text-red-500 text-center mt-10">{error}</p>;

  return (
    <div className="p-4 overflow-x-auto max-w-screen">
      <h1 className="text-2xl font-bold mb-4 text-center">Price Grid</h1>

      <div className="flex justify-between mb-4">
        <button
          onClick={() => setShowModal(true)}
          className="bg-blue-600 hover:bg-blue-700 text-white font-semibold px-4 py-2 rounded"
        >
          Add Price
        </button>
        {!isEditing ? (
          <button
            onClick={() => setIsEditing(true)}
            className="bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded"
          >
            Edit Prices
          </button>
        ) : (
          <div className="flex space-x-2">
            <button
              onClick={handleSaveChanges}
              className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded"
            >
              Save Changes
            </button>
            <button
              onClick={() => {
                setIsEditing(false);
                setEditedCells([]);
              }}
              className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded"
            >
              Cancel
            </button>
          </div>
        )}
      </div>

      <table className="min-w-max w-full border-collapse border border-gray-300 text-sm md:text-base">
        <thead>
          <tr className="bg-gray-200 sticky top-0 z-10">
            <th className="sticky left-0 z-20 bg-gray-200 border border-gray-300 px-4 py-2">
              Height ↓<br />Width →
            </th>
            {gridData?.widths.map((width: number, idx: number) => (
              <th key={idx} className="border border-gray-300 px-4 py-2 text-center">
                {width}"
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {gridData?.prices.map((row, rowIdx: number) => (
            <tr key={rowIdx} className={rowIdx % 2 === 0 ? 'bg-white' : 'bg-gray-50'}>
              <td className="sticky left-0 z-10 bg-white border border-gray-300 px-4 py-2 font-bold">
                {row.height}"
              </td>
              {row.values.map((val, colIdx: number) => {
                const width = gridData.widths[colIdx];
                const existingEdit = editedCells.find(cell => cell.height === row.height && cell.width === width);
                const currentVal = existingEdit ? existingEdit.price : val;

                return (
                  <td key={colIdx} className="border border-gray-300 px-2 py-1 text-center">
                    {isEditing ? (
                      <input
                        type="number"
                        value={currentVal}
                        onChange={(e) => {
                          const newPrice = parseFloat(e.target.value);
                          setEditedCells(prev => {
                            const updated = [...prev];
                            const index = updated.findIndex(c => c.height === row.height && c.width === width);
                            if (index >= 0) updated[index].price = newPrice;
                            else updated.push({ height: row.height, width, price: newPrice });
                            return updated;
                          });
                        }}
                        className="w-full border px-1 py-1 text-sm"
                      />
                    ) : (
                      val !== 0 ? `$${val}` : '-'
                    )}
                  </td>
                );
              })}
            </tr>
          ))}
        </tbody>
      </table>

      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
          <div className="bg-white p-6 rounded-lg w-[90%] max-w-md">
            <h2 className="text-xl font-bold mb-4 text-center">Add Price</h2>
            <div className="space-y-4">
              <input
                type="number"
                placeholder="Height"
                value={formData.height}
                onChange={(e) => setFormData({ ...formData, height: e.target.value })}
                className="w-full border border-gray-300 px-4 py-2 rounded"
              />
              <input
                type="number"
                placeholder="Width"
                value={formData.width}
                onChange={(e) => setFormData({ ...formData, width: e.target.value })}
                className="w-full border border-gray-300 px-4 py-2 rounded"
              />
              <input
                type="number"
                placeholder="Price"
                value={formData.price}
                onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                className="w-full border border-gray-300 px-4 py-2 rounded"
              />
              <div className="flex justify-end space-x-2 pt-2">
                <button
                  onClick={() => setShowModal(false)}
                  className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
                >
                  Cancel
                </button>
                <button
                  onClick={handleAdd}
                  className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                >
                  Submit
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
