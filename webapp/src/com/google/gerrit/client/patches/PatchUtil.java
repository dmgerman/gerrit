begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.patches
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|patches
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
name|client
operator|.
name|ui
operator|.
name|DomUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|JsonUtil
import|;
end_import

begin_class
DECL|class|PatchUtil
specifier|public
class|class
name|PatchUtil
block|{
DECL|field|C
specifier|public
specifier|static
specifier|final
name|PatchConstants
name|C
init|=
name|GWT
operator|.
name|create
argument_list|(
name|PatchConstants
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DETAIL_SVC
specifier|public
specifier|static
specifier|final
name|PatchDetailService
name|DETAIL_SVC
decl_stmt|;
static|static
block|{
name|DETAIL_SVC
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|PatchDetailService
operator|.
name|class
argument_list|)
expr_stmt|;
name|JsonUtil
operator|.
name|bind
argument_list|(
name|DETAIL_SVC
argument_list|,
literal|"rpc/PatchDetailService"
argument_list|)
expr_stmt|;
block|}
DECL|method|lineToHTML (final String src)
specifier|public
specifier|static
name|String
name|lineToHTML
parameter_list|(
specifier|final
name|String
name|src
parameter_list|)
block|{
name|String
name|html
init|=
name|DomUtil
operator|.
name|escape
argument_list|(
name|src
argument_list|)
decl_stmt|;
name|html
operator|=
name|expandTabs
argument_list|(
name|html
argument_list|)
expr_stmt|;
return|return
name|html
return|;
block|}
DECL|method|expandTabs (String src)
specifier|private
specifier|native
specifier|static
name|String
name|expandTabs
parameter_list|(
name|String
name|src
parameter_list|)
comment|/*-{ return src.replace(/\t/g, '<span title="Visual Tab" class="gerrit-visualtab">&raquo;</span>\t'); }-*/
function_decl|;
block|}
end_class

end_unit

