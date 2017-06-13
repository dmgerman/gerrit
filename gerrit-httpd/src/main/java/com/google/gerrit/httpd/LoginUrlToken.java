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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CharMatcher
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
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
import|;
end_import

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

begin_class
DECL|class|LoginUrlToken
specifier|public
class|class
name|LoginUrlToken
block|{
DECL|field|DEFAULT_TOKEN
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_TOKEN
init|=
literal|'#'
operator|+
name|PageLinks
operator|.
name|MINE
decl_stmt|;
DECL|method|getToken (HttpServletRequest req)
specifier|public
specifier|static
name|String
name|getToken
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
name|String
name|token
init|=
name|req
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|token
argument_list|)
condition|)
block|{
return|return
name|DEFAULT_TOKEN
return|;
block|}
return|return
name|CharMatcher
operator|.
name|is
argument_list|(
literal|'/'
argument_list|)
operator|.
name|trimLeadingFrom
argument_list|(
name|token
argument_list|)
return|;
block|}
DECL|method|LoginUrlToken ()
specifier|private
name|LoginUrlToken
parameter_list|()
block|{}
block|}
end_class

end_unit

