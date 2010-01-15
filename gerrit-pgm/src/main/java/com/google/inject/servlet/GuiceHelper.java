begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.inject.servlet
package|package
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|servlet
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|servlet
operator|.
name|GuiceFilter
operator|.
name|Context
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_class
DECL|class|GuiceHelper
specifier|public
class|class
name|GuiceHelper
block|{
DECL|method|runInContext (HttpServletRequest req, HttpServletResponse rsp, Runnable thunk)
specifier|public
specifier|static
name|void
name|runInContext
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|rsp
parameter_list|,
name|Runnable
name|thunk
parameter_list|)
block|{
name|Context
name|previous
init|=
name|GuiceFilter
operator|.
name|localContext
operator|.
name|get
argument_list|()
decl_stmt|;
try|try
block|{
name|GuiceFilter
operator|.
name|localContext
operator|.
name|set
argument_list|(
operator|new
name|Context
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|)
argument_list|)
expr_stmt|;
name|thunk
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|GuiceFilter
operator|.
name|localContext
operator|.
name|set
argument_list|(
name|previous
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|GuiceHelper ()
specifier|private
name|GuiceHelper
parameter_list|()
block|{   }
block|}
end_class

end_unit

