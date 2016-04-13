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
DECL|package|com.google.gerrit.server.cache
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
package|;
end_package

begin_interface
DECL|interface|PersistentCache
specifier|public
interface|interface
name|PersistentCache
block|{
DECL|method|diskStats ()
name|DiskStats
name|diskStats
parameter_list|()
function_decl|;
DECL|class|DiskStats
class|class
name|DiskStats
block|{
DECL|field|size
specifier|private
specifier|final
name|long
name|size
decl_stmt|;
DECL|field|space
specifier|private
specifier|final
name|long
name|space
decl_stmt|;
DECL|field|hitCount
specifier|private
specifier|final
name|long
name|hitCount
decl_stmt|;
DECL|field|missCount
specifier|private
specifier|final
name|long
name|missCount
decl_stmt|;
DECL|method|DiskStats (long size, long space, long hitCount, long missCount)
specifier|public
name|DiskStats
parameter_list|(
name|long
name|size
parameter_list|,
name|long
name|space
parameter_list|,
name|long
name|hitCount
parameter_list|,
name|long
name|missCount
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
name|this
operator|.
name|space
operator|=
name|space
expr_stmt|;
name|this
operator|.
name|hitCount
operator|=
name|hitCount
expr_stmt|;
name|this
operator|.
name|missCount
operator|=
name|missCount
expr_stmt|;
block|}
DECL|method|size ()
specifier|public
name|long
name|size
parameter_list|()
block|{
return|return
name|size
return|;
block|}
DECL|method|space ()
specifier|public
name|long
name|space
parameter_list|()
block|{
return|return
name|space
return|;
block|}
DECL|method|hitCount ()
specifier|public
name|long
name|hitCount
parameter_list|()
block|{
return|return
name|hitCount
return|;
block|}
DECL|method|requestCount ()
specifier|public
name|long
name|requestCount
parameter_list|()
block|{
return|return
name|hitCount
operator|+
name|missCount
return|;
block|}
block|}
block|}
end_interface

end_unit

