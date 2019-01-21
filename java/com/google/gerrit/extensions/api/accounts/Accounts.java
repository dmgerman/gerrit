begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.extensions.api.accounts
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|accounts
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|ListAccountsOption
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
operator|.
name|AccountInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|NotImplementedException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_interface
DECL|interface|Accounts
specifier|public
interface|interface
name|Accounts
block|{
comment|/**    * Look up an account by ID.    *    *<p><strong>Note:</strong> This method eagerly reads the account. Methods that mutate the    * account do not necessarily re-read the account. Therefore, calling a getter method on an    * instance after calling a mutation method on that same instance is not guaranteed to reflect the    * mutation. It is not recommended to store references to {@code AccountApi} instances.    *    * @param id any identifier supported by the REST API, including numeric ID, email, or username.    * @return API for accessing the account.    * @throws RestApiException if an error occurred.    */
DECL|method|id (String id)
name|AccountApi
name|id
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** @see #id(String) */
DECL|method|id (int id)
name|AccountApi
name|id
parameter_list|(
name|int
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Look up the account of the current in-scope user.    *    * @see #id(String)    */
DECL|method|self ()
name|AccountApi
name|self
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/** Create a new account with the given username and default options. */
DECL|method|create (String username)
name|AccountApi
name|create
parameter_list|(
name|String
name|username
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** Create a new account. */
DECL|method|create (AccountInput input)
name|AccountApi
name|create
parameter_list|(
name|AccountInput
name|input
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Suggest users for a given query.    *    *<p>Example code: {@code suggestAccounts().withQuery("Reviewer").withLimit(5).get()}    *    * @return API for setting parameters and getting result.    */
DECL|method|suggestAccounts ()
name|SuggestAccountsRequest
name|suggestAccounts
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Suggest users for a given query.    *    *<p>Shortcut API for {@code suggestAccounts().withQuery(String)}.    *    * @see #suggestAccounts()    */
DECL|method|suggestAccounts (String query)
name|SuggestAccountsRequest
name|suggestAccounts
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Query users.    *    *<p>Example code: {@code query().withQuery("name:John email:example.com").withLimit(5).get()}    *    * @return API for setting parameters and getting result.    */
DECL|method|query ()
name|QueryRequest
name|query
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Query users.    *    *<p>Shortcut API for {@code query().withQuery(String)}.    *    * @see #query()    */
DECL|method|query (String query)
name|QueryRequest
name|query
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * API for setting parameters and getting result. Used for {@code suggestAccounts()}.    *    * @see #suggestAccounts()    */
DECL|class|SuggestAccountsRequest
specifier|abstract
class|class
name|SuggestAccountsRequest
block|{
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
comment|/** Execute query and return a list of accounts. */
DECL|method|get ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**      * Set query.      *      * @param query needs to be in human-readable form.      */
DECL|method|withQuery (String query)
specifier|public
name|SuggestAccountsRequest
name|withQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set limit for returned list of accounts. Optional; server-default is used when not provided.      */
DECL|method|withLimit (int limit)
specifier|public
name|SuggestAccountsRequest
name|withLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|getLimit ()
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
block|}
comment|/**    * API for setting parameters and getting result. Used for {@code query()}.    *    * @see #query()    */
DECL|class|QueryRequest
specifier|abstract
class|class
name|QueryRequest
block|{
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
DECL|field|start
specifier|private
name|int
name|start
decl_stmt|;
DECL|field|suggest
specifier|private
name|boolean
name|suggest
decl_stmt|;
DECL|field|options
specifier|private
name|EnumSet
argument_list|<
name|ListAccountsOption
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListAccountsOption
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/** Execute query and return a list of accounts. */
DECL|method|get ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**      * Set query.      *      * @param query needs to be in human-readable form.      */
DECL|method|withQuery (String query)
specifier|public
name|QueryRequest
name|withQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set limit for returned list of accounts. Optional; server-default is used when not provided.      */
DECL|method|withLimit (int limit)
specifier|public
name|QueryRequest
name|withLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Set number of accounts to skip. Optional; no accounts are skipped when not provided. */
DECL|method|withStart (int start)
specifier|public
name|QueryRequest
name|withStart
parameter_list|(
name|int
name|start
parameter_list|)
block|{
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withSuggest (boolean suggest)
specifier|public
name|QueryRequest
name|withSuggest
parameter_list|(
name|boolean
name|suggest
parameter_list|)
block|{
name|this
operator|.
name|suggest
operator|=
name|suggest
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Set an option on the request, appending to existing options. */
DECL|method|withOption (ListAccountsOption options)
specifier|public
name|QueryRequest
name|withOption
parameter_list|(
name|ListAccountsOption
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|.
name|add
argument_list|(
name|options
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Set options on the request, appending to existing options. */
DECL|method|withOptions (ListAccountsOption... options)
specifier|public
name|QueryRequest
name|withOptions
parameter_list|(
name|ListAccountsOption
modifier|...
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|options
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Set options on the request, replacing existing options. */
DECL|method|withOptions (EnumSet<ListAccountsOption> options)
specifier|public
name|QueryRequest
name|withOptions
parameter_list|(
name|EnumSet
argument_list|<
name|ListAccountsOption
argument_list|>
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|getLimit ()
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
DECL|method|getStart ()
specifier|public
name|int
name|getStart
parameter_list|()
block|{
return|return
name|start
return|;
block|}
DECL|method|getSuggest ()
specifier|public
name|boolean
name|getSuggest
parameter_list|()
block|{
return|return
name|suggest
return|;
block|}
DECL|method|getOptions ()
specifier|public
name|EnumSet
argument_list|<
name|ListAccountsOption
argument_list|>
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
block|}
comment|/**    * A default implementation which allows source compatibility when adding new methods to the    * interface.    */
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|Accounts
block|{
annotation|@
name|Override
DECL|method|id (String id)
specifier|public
name|AccountApi
name|id
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|id (int id)
specifier|public
name|AccountApi
name|id
parameter_list|(
name|int
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|self ()
specifier|public
name|AccountApi
name|self
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|create (String username)
specifier|public
name|AccountApi
name|create
parameter_list|(
name|String
name|username
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|create (AccountInput input)
specifier|public
name|AccountApi
name|create
parameter_list|(
name|AccountInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|suggestAccounts ()
specifier|public
name|SuggestAccountsRequest
name|suggestAccounts
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|suggestAccounts (String query)
specifier|public
name|SuggestAccountsRequest
name|suggestAccounts
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|query ()
specifier|public
name|QueryRequest
name|query
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|query (String query)
specifier|public
name|QueryRequest
name|query
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
block|}
block|}
end_interface

end_unit

