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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
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
name|patches
operator|.
name|PatchSideBySideScreen
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
name|client
operator|.
name|reviewdb
operator|.
name|Patch
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

begin_comment
comment|/** Detail necessary to display {@link PatchSideBySideScreen}. */
end_comment

begin_class
DECL|class|SideBySidePatchDetail
specifier|public
class|class
name|SideBySidePatchDetail
extends|extends
name|BasePatchDetail
block|{
DECL|field|fileCount
specifier|protected
name|int
name|fileCount
decl_stmt|;
DECL|field|lineCount
specifier|protected
name|int
name|lineCount
decl_stmt|;
DECL|field|lines
specifier|protected
name|List
argument_list|<
name|List
argument_list|<
name|SideBySideLine
argument_list|>
argument_list|>
name|lines
decl_stmt|;
DECL|method|SideBySidePatchDetail ()
specifier|protected
name|SideBySidePatchDetail
parameter_list|()
block|{   }
DECL|method|SideBySidePatchDetail (final Patch p, final AccountInfoCache aic)
specifier|public
name|SideBySidePatchDetail
parameter_list|(
specifier|final
name|Patch
name|p
parameter_list|,
specifier|final
name|AccountInfoCache
name|aic
parameter_list|)
block|{
name|super
argument_list|(
name|p
argument_list|,
name|aic
argument_list|)
expr_stmt|;
block|}
DECL|method|getFileCount ()
specifier|public
name|int
name|getFileCount
parameter_list|()
block|{
return|return
name|fileCount
return|;
block|}
DECL|method|getLineCount ()
specifier|public
name|int
name|getLineCount
parameter_list|()
block|{
return|return
name|lineCount
return|;
block|}
DECL|method|getLines ()
specifier|public
name|List
argument_list|<
name|List
argument_list|<
name|SideBySideLine
argument_list|>
argument_list|>
name|getLines
parameter_list|()
block|{
return|return
name|lines
return|;
block|}
DECL|method|setLines (final int fc, final int lc, final List<List<SideBySideLine>> in)
specifier|public
name|void
name|setLines
parameter_list|(
specifier|final
name|int
name|fc
parameter_list|,
specifier|final
name|int
name|lc
parameter_list|,
specifier|final
name|List
argument_list|<
name|List
argument_list|<
name|SideBySideLine
argument_list|>
argument_list|>
name|in
parameter_list|)
block|{
name|fileCount
operator|=
name|fc
expr_stmt|;
name|lineCount
operator|=
name|lc
expr_stmt|;
name|lines
operator|=
name|in
expr_stmt|;
block|}
block|}
end_class

end_unit

