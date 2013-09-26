begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_class
DECL|class|CanonicalWebUrl
specifier|public
class|class
name|CanonicalWebUrl
block|{
DECL|field|configured
specifier|private
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|configured
decl_stmt|;
annotation|@
name|Inject
DECL|method|CanonicalWebUrl ( @om.google.gerrit.server.config.CanonicalWebUrl @ullable Provider<String> provider)
name|CanonicalWebUrl
parameter_list|(
annotation|@
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|CanonicalWebUrl
annotation|@
name|Nullable
name|Provider
argument_list|<
name|String
argument_list|>
name|provider
parameter_list|)
block|{
name|configured
operator|=
name|provider
expr_stmt|;
block|}
DECL|method|get (HttpServletRequest req)
specifier|public
name|String
name|get
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
name|String
name|url
init|=
name|configured
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|url
operator|!=
literal|null
condition|?
name|url
else|:
name|computeFromRequest
argument_list|(
name|req
argument_list|)
return|;
block|}
DECL|method|computeFromRequest (HttpServletRequest req)
specifier|static
name|String
name|computeFromRequest
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
name|StringBuffer
name|url
init|=
name|req
operator|.
name|getRequestURL
argument_list|()
decl_stmt|;
name|url
operator|.
name|setLength
argument_list|(
name|url
operator|.
name|length
argument_list|()
operator|-
name|req
operator|.
name|getServletPath
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|.
name|charAt
argument_list|(
name|url
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|!=
literal|'/'
condition|)
block|{
name|url
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
return|return
name|url
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

