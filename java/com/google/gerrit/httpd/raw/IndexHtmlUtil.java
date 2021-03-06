begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.raw
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|raw
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|template
operator|.
name|soy
operator|.
name|data
operator|.
name|ordainers
operator|.
name|GsonOrdainer
operator|.
name|serializeObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
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
name|common
operator|.
name|Nullable
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
name|common
operator|.
name|UsedAt
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
name|common
operator|.
name|UsedAt
operator|.
name|Project
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
name|api
operator|.
name|GerritApi
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
name|api
operator|.
name|accounts
operator|.
name|AccountApi
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
name|api
operator|.
name|config
operator|.
name|Server
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
name|AuthException
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|json
operator|.
name|OutputFormat
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|Gson
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|template
operator|.
name|soy
operator|.
name|data
operator|.
name|SanitizedContent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/** Helper for generating parts of {@code index.html}. */
end_comment

begin_class
DECL|class|IndexHtmlUtil
specifier|public
class|class
name|IndexHtmlUtil
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
comment|/**    * Returns both static and dynamic parameters of {@code index.html}. The result is to be used when    * rendering the soy template.    */
DECL|method|templateData ( GerritApi gerritApi, String canonicalURL, String cdnPath, String faviconPath, Map<String, String[]> urlParameterMap, Function<String, SanitizedContent> urlInScriptTagOrdainer)
specifier|public
specifier|static
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|templateData
parameter_list|(
name|GerritApi
name|gerritApi
parameter_list|,
name|String
name|canonicalURL
parameter_list|,
name|String
name|cdnPath
parameter_list|,
name|String
name|faviconPath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|urlParameterMap
parameter_list|,
name|Function
argument_list|<
name|String
argument_list|,
name|SanitizedContent
argument_list|>
name|urlInScriptTagOrdainer
parameter_list|)
throws|throws
name|URISyntaxException
throws|,
name|RestApiException
block|{
return|return
name|ImmutableMap
operator|.
expr|<
name|String
operator|,
name|Object
operator|>
name|builder
argument_list|()
operator|.
name|putAll
argument_list|(
name|staticTemplateData
argument_list|(
name|canonicalURL
argument_list|,
name|cdnPath
argument_list|,
name|faviconPath
argument_list|,
name|urlParameterMap
argument_list|,
name|urlInScriptTagOrdainer
argument_list|)
argument_list|)
operator|.
name|putAll
argument_list|(
name|dynamicTemplateData
argument_list|(
name|gerritApi
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/** Returns dynamic parameters of {@code index.html}. */
annotation|@
name|UsedAt
argument_list|(
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|dynamicTemplateData (GerritApi gerritApi)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|SanitizedContent
argument_list|>
argument_list|>
name|dynamicTemplateData
parameter_list|(
name|GerritApi
name|gerritApi
parameter_list|)
throws|throws
name|RestApiException
block|{
name|Gson
name|gson
init|=
name|OutputFormat
operator|.
name|JSON_COMPACT
operator|.
name|newGson
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|SanitizedContent
argument_list|>
name|initialData
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Server
name|serverApi
init|=
name|gerritApi
operator|.
name|config
argument_list|()
operator|.
name|server
argument_list|()
decl_stmt|;
name|initialData
operator|.
name|put
argument_list|(
literal|"\"/config/server/info\""
argument_list|,
name|serializeObject
argument_list|(
name|gson
argument_list|,
name|serverApi
operator|.
name|getInfo
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|initialData
operator|.
name|put
argument_list|(
literal|"\"/config/server/version\""
argument_list|,
name|serializeObject
argument_list|(
name|gson
argument_list|,
name|serverApi
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|initialData
operator|.
name|put
argument_list|(
literal|"\"/config/server/top-menus\""
argument_list|,
name|serializeObject
argument_list|(
name|gson
argument_list|,
name|serverApi
operator|.
name|topMenus
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|AccountApi
name|accountApi
init|=
name|gerritApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
decl_stmt|;
name|initialData
operator|.
name|put
argument_list|(
literal|"\"/accounts/self/detail\""
argument_list|,
name|serializeObject
argument_list|(
name|gson
argument_list|,
name|accountApi
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|initialData
operator|.
name|put
argument_list|(
literal|"\"/accounts/self/preferences\""
argument_list|,
name|serializeObject
argument_list|(
name|gson
argument_list|,
name|accountApi
operator|.
name|getPreferences
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|initialData
operator|.
name|put
argument_list|(
literal|"\"/accounts/self/preferences.diff\""
argument_list|,
name|serializeObject
argument_list|(
name|gson
argument_list|,
name|accountApi
operator|.
name|getDiffPreferences
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|initialData
operator|.
name|put
argument_list|(
literal|"\"/accounts/self/preferences.edit\""
argument_list|,
name|serializeObject
argument_list|(
name|gson
argument_list|,
name|accountApi
operator|.
name|getEditPreferences
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Can't inline account-related data because user is unauthenticated"
argument_list|)
expr_stmt|;
comment|// Don't render data
comment|// TODO(hiesel): Tell the client that the user is not authenticated so that it doesn't have to
comment|// fetch anyway. This requires more client side modifications.
block|}
return|return
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"gerritInitialData"
argument_list|,
name|initialData
argument_list|)
return|;
block|}
comment|/** Returns all static parameters of {@code index.html}. */
DECL|method|staticTemplateData ( String canonicalURL, String cdnPath, String faviconPath, Map<String, String[]> urlParameterMap, Function<String, SanitizedContent> urlInScriptTagOrdainer)
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|staticTemplateData
parameter_list|(
name|String
name|canonicalURL
parameter_list|,
name|String
name|cdnPath
parameter_list|,
name|String
name|faviconPath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|urlParameterMap
parameter_list|,
name|Function
argument_list|<
name|String
argument_list|,
name|SanitizedContent
argument_list|>
name|urlInScriptTagOrdainer
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
name|canonicalPath
init|=
name|computeCanonicalPath
argument_list|(
name|canonicalURL
argument_list|)
decl_stmt|;
name|String
name|staticPath
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|cdnPath
operator|!=
literal|null
condition|)
block|{
name|staticPath
operator|=
name|cdnPath
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|canonicalPath
operator|!=
literal|null
condition|)
block|{
name|staticPath
operator|=
name|canonicalPath
expr_stmt|;
block|}
name|SanitizedContent
name|sanitizedStaticPath
init|=
name|urlInScriptTagOrdainer
operator|.
name|apply
argument_list|(
name|staticPath
argument_list|)
decl_stmt|;
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|data
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
if|if
condition|(
name|canonicalPath
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|put
argument_list|(
literal|"canonicalPath"
argument_list|,
name|canonicalPath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sanitizedStaticPath
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|put
argument_list|(
literal|"staticResourcePath"
argument_list|,
name|sanitizedStaticPath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|faviconPath
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|put
argument_list|(
literal|"faviconPath"
argument_list|,
name|faviconPath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|urlParameterMap
operator|.
name|containsKey
argument_list|(
literal|"ce"
argument_list|)
condition|)
block|{
name|data
operator|.
name|put
argument_list|(
literal|"polyfillCE"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|urlParameterMap
operator|.
name|containsKey
argument_list|(
literal|"sd"
argument_list|)
condition|)
block|{
name|data
operator|.
name|put
argument_list|(
literal|"polyfillSD"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|urlParameterMap
operator|.
name|containsKey
argument_list|(
literal|"sc"
argument_list|)
condition|)
block|{
name|data
operator|.
name|put
argument_list|(
literal|"polyfillSC"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
return|return
name|data
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|computeCanonicalPath (@ullable String canonicalURL)
specifier|private
specifier|static
name|String
name|computeCanonicalPath
parameter_list|(
annotation|@
name|Nullable
name|String
name|canonicalURL
parameter_list|)
throws|throws
name|URISyntaxException
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|canonicalURL
argument_list|)
condition|)
block|{
return|return
literal|""
return|;
block|}
comment|// If we serving from a sub-directory rather than root, determine the path
comment|// from the cannonical web URL.
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|canonicalURL
argument_list|)
decl_stmt|;
return|return
name|uri
operator|.
name|getPath
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"/$"
argument_list|,
literal|""
argument_list|)
return|;
block|}
DECL|method|IndexHtmlUtil ()
specifier|private
name|IndexHtmlUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

