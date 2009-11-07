begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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

begin_class
DECL|class|Main
specifier|public
specifier|final
class|class
name|Main
block|{
comment|// We don't do any real work here because we need to import
comment|// the archive lookup code and we cannot import a class in
comment|// the default package.  So this is just a tiny springboard
comment|// to jump into the real main code.
comment|//
DECL|method|main (final String argv[])
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
name|argv
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|main
operator|.
name|GerritLauncher
operator|.
name|main
argument_list|(
name|argv
argument_list|)
expr_stmt|;
block|}
DECL|method|Main ()
specifier|private
name|Main
parameter_list|()
block|{   }
block|}
end_class

end_unit

