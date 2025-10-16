# VideoSDK Knowledge Base for LLM Assistant  
  
[VideoSDK Docs](https://docs.videosdk.live/)  
  
## Overview  
  
This project contains a comprehensive knowledge base about VideoSDK, a real-time communication platform. The data is specifically organized and formatted for use by Large Language Model (LLM) assistants to provide accurate, up-to-date information about VideoSDK products, services, and troubleshooting.  
  
## Data Sources  
  
### 1. VideoSDK Codebase (`videosdk-live/` directory)  
- **Source**: VideoSDK GitHub repositories (videosdk-live org)  
- **Content**: Complete source code for the VideoSDK ecosystem  
- **Coverage**: SDKs, examples, and integration code  
- **Update script**: `python videosdk_repos_update.py`  
- **Update frequency**: Updated repositories from past 365 days, excludes archived repos  
  
### 2. VideoSDK Documentation (`doc/` directory)  
- **Source**: Official VideoSDK documentation (https://docs.videosdk.live/llms-full.txt)  
- **Content**: Complete LLM-optimized documentation  
- **Format**: Plain text format optimized for language models  
- **Update script**: `python videosdk_repos_update.py` (included in repo sync)  
  
## How to Use This Data as an LLM Assistant  
  
### When Answering VideoSDK Questions  
  
1. **Check the documentation** (`doc/full-llm.txt`)  
   - Contains complete VideoSDK documentation  
   - Use for detailed explanations of concepts and APIs  
   - Cross-reference with code examples for complete answers  
  
2. **Use codebase for technical implementation details**  
   - Reference actual code examples from the `videosdk-live/` directory  
   - Show real API usage patterns from working examples  
   - Provide accurate method signatures and parameters  
  
3. **Reference specific files when relevant**  
   - Provide direct quotes from the documentation when helpful  
   - Link to specific code examples using file paths  
  
### Code Examples and Implementation  
  
- **SDKs**: Various client-side and server-side implementations in `videosdk-live/`  
- **Examples**: Working examples demonstrating VideoSDK usage  
- **Integration code**: Real-world integration patterns  
  
## Response Guidelines  
  
### Do:  
- ✅ Quote directly from documentation when available  
- ✅ Provide specific file references for code examples  
- ✅ Reference working examples from `videosdk-live/` when showing implementation  
- ✅ Mention when information comes from official VideoSDK documentation  
- ✅ Suggest checking the documentation for the most current information  
  
### Don't:  
- ❌ Make up API methods - reference the actual codebase  
- ❌ Provide unofficial workarounds without noting they're not from VideoSDK docs  
- ❌ Guess at implementation details - use actual code examples  
  
## Data Freshness  
  
- Documentation is downloaded from the official VideoSDK docs  
- Run the update script regularly to ensure current information  
- Codebase represents snapshots of VideoSDK repositories  
- When in doubt about currency, recommend checking the official VideoSDK documentation  
  
## Updating the Knowledge Base  
  
To update all data sources:  
  
```bash  
python videosdk_repos_update.py
```

This will:

1. Download the latest VideoSDK documentation
2. Fetch all public repositories from the videosdk-live organization
3. Clone repositories updated in the last 365 days (excluding archived repos)

### Setting up GitHub Token (Optional but Recommended)
To avoid API rate limits:

```name=".env"
GITHUB_TOKEN=your_github_token_here
```

## Repository Structure
```
videosdk-wiki/  
├── videosdk-live/          # VideoSDK repositories  
│   ├── repo1/  
│   ├── repo2/  
│   └── ...  
├── doc/  
│   └── full-llm.txt        # Complete LLM documentation  
├── videosdk_repos_update.py  # Repository sync script  
├── requirements.txt        # Python dependencies  
└── README.md              # This file  
```

## Getting Help
For the most current information, users should:

- Check the official VideoSDK documentation: https://docs.videosdk.live
- Review the GitHub repositories for latest code examples
- Visit the VideoSDK website for support resources
This knowledge base provides a solid foundation, but always encourage users to verify with official sources for production deployments.