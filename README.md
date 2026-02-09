# Invoice Parser CLI

A professional Java command-line application for extracting structured data from invoice PDFs and CSV files. Built with enterprise-grade libraries including Apache PDFBox, OpenCSV, and Gson.

## Features

✨ **Multi-Format Support**
- Parse PDF invoices using Apache PDFBox
- Parse CSV invoices using OpenCSV
- Auto-detect file format

📊 **Flexible Output**
- Export to JSON format
- Export to CSV format
- Export to both formats simultaneously

🔍 **Smart Extraction**
- Invoice numbers and dates
- Vendor and customer information
- Line items with quantities and prices
- Subtotals, taxes, and totals
- Data validation (subtotal + tax = total)

⚡ **Batch Processing**
- Process single files
- Process entire directories
- Progress tracking and error reporting

🧪 **Well-Tested**
- Comprehensive JUnit test suite
- Input validation
- Error handling

## Prerequisites

- **Java**: JDK 11 or higher
- **Maven**: 3.6 or higher

## Installation

### Clone the Repository

```bash
git clone https://github.com/yourusername/invoice-parser-java.git
cd invoice-parser-java
```

### Build the Project

```bash
mvn clean package
```

This creates an executable JAR file: `target/invoice-parser.jar`

### Run Tests

```bash
mvn test
```

## Usage

### Basic Usage

```bash
java -jar target/invoice-parser.jar --input invoice.pdf --output result.json
```

### Command-Line Options

| Option | Shorthand | Description | Default |
|--------|-----------|-------------|---------|
| `--input` | `-i` | Input file or directory (required) | - |
| `--output` | `-o` | Output file or directory | `output` |
| `--format` | `-f` | Output format: `json`, `csv`, or `both` | `json` |
| `--verbose` | `-v` | Print extracted data to console | `false` |
| `--help` | `-h` | Show help message | - |

### Examples

#### Parse Single PDF to JSON

```bash
java -jar target/invoice-parser.jar \
  --input sample-invoices/invoice.pdf \
  --output results/invoice.json
```

#### Parse to Both JSON and CSV

```bash
java -jar target/invoice-parser.jar \
  -i invoice.pdf \
  -o result \
  -f both
```

This creates:
- `result.json`
- `result.csv`

#### Process Entire Directory

```bash
java -jar target/invoice-parser.jar \
  -i sample-invoices/ \
  -o results/ \
  -f json
```

#### Verbose Output (for Debugging)

```bash
java -jar target/invoice-parser.jar \
  -i invoice.pdf \
  -o result.json \
  -v
```

## Project Structure

```
invoice-parser-java/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/invoiceparser/
│   │           ├── Main.java                    # CLI entry point
│   │           ├── InvoiceParser.java           # Main parser orchestrator
│   │           ├── models/
│   │           │   ├── InvoiceData.java         # Invoice data model
│   │           │   └── LineItem.java            # Line item model
│   │           ├── extractors/
│   │           │   ├── PDFExtractor.java        # PDF parsing logic
│   │           │   └── CSVExtractor.java        # CSV parsing logic
│   │           └── formatters/
│   │               └── OutputFormatter.java     # Output formatting
│   └── test/
│       └── java/
│           └── com/invoiceparser/
│               └── InvoiceParserTest.java       # Unit tests
├── sample-invoices/                             # Sample files for testing
├── pom.xml                                      # Maven configuration
├── .gitignore
└── README.md
```

## Output Format

### JSON Output

```json
{
  "invoiceNumber": "INV-2024-0042",
  "invoiceDate": "2024-01-15",
  "vendorName": "ACME CORPORATION",
  "vendorAddress": "123 Business Avenue, Ottawa, ON",
  "subtotal": 7100.0,
  "taxAmount": 1063.23,
  "totalAmount": 8163.23,
  "items": [
    {
      "description": "Software Development Services",
      "quantity": 40,
      "unitPrice": 150.0,
      "lineTotal": 6000.0
    }
  ]
}
```

### CSV Output

```csv
Invoice Metadata
Invoice Number,INV-2024-0042
Date,2024-01-15
Vendor,ACME CORPORATION
Subtotal,7100.0
Tax,1063.23
Total,8163.23

Line Items
Description,Quantity,Unit Price,Line Total
Software Development Services,40,150.00,6000.00
Cloud Hosting - Annual,12,50.00,600.00
```

## Technologies Used

- **Java 11**: Core programming language
- **Maven**: Build automation and dependency management
- **Apache PDFBox 2.0.29**: PDF text extraction
- **OpenCSV 5.7.1**: CSV file parsing
- **Gson 2.10.1**: JSON serialization/deserialization
- **JUnit 5**: Unit testing framework

## Development

### Adding New Features

1. **Custom Extraction Rules**: Modify regex patterns in `PDFExtractor.java`
2. **New Output Formats**: Extend `OutputFormatter.java`
3. **Additional Fields**: Update `InvoiceData.java` model

### Running in Development Mode

```bash
mvn compile exec:java -Dexec.mainClass="com.invoiceparser.Main" \
  -Dexec.args="--input sample-invoices/example-invoice.csv --output output/result.json -v"
```

## Testing

Run the complete test suite:

```bash
mvn test
```

Run specific test class:

```bash
mvn test -Dtest=InvoiceParserTest
```

## Limitations & Future Improvements

**Current Limitations:**
- PDF parsing relies on text extraction (won't work on scanned/image PDFs)
- CSV format expects specific structure
- Line item extraction uses simplified regex patterns

**Planned Features:**
- [ ] OCR support for scanned PDFs
- [ ] Machine learning-based field extraction
- [ ] Support for Excel files (.xlsx)
- [ ] Web API interface
- [ ] Database storage option
- [ ] Invoice template detection
- [ ] Multi-language support

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

**Pierre Teyou Gaiss**
- GitHub: [@pteyo032](https://github.com/pteyo032)
- LinkedIn: [teyou-software-engineer](https://linkedin.com/in/teyou-software-engineer)
- Email: pteyo032@uottawa.ca

## Acknowledgments

- Built as part of Software Engineering coursework at University of Ottawa
- Inspired by real-world automation needs in business invoice processing
- Thanks to the open-source community for excellent libraries

---

**Note**: This is a learning project demonstrating Java programming, OOP principles, file I/O, regex parsing, and CLI development. For production use, consider more robust commercial solutions or machine learning-based extraction tools.
