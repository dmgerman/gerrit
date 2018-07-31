begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertWithMessage
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|extensions
operator|.
name|proto
operator|.
name|ProtoTruth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
operator|.
name|testing
operator|.
name|CacheSerializerTestUtil
operator|.
name|bytes
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
name|ImmutableSortedSet
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
name|Streams
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
name|reviewdb
operator|.
name|client
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
name|server
operator|.
name|cache
operator|.
name|proto
operator|.
name|Cache
operator|.
name|TagSetHolderProto
operator|.
name|TagSetProto
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
name|server
operator|.
name|cache
operator|.
name|proto
operator|.
name|Cache
operator|.
name|TagSetHolderProto
operator|.
name|TagSetProto
operator|.
name|CachedRefProto
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
name|server
operator|.
name|cache
operator|.
name|proto
operator|.
name|Cache
operator|.
name|TagSetHolderProto
operator|.
name|TagSetProto
operator|.
name|TagProto
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
name|server
operator|.
name|git
operator|.
name|TagSet
operator|.
name|CachedRef
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
name|server
operator|.
name|git
operator|.
name|TagSet
operator|.
name|Tag
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
name|BitSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectIdOwnerMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|TagSetTest
specifier|public
class|class
name|TagSetTest
block|{
annotation|@
name|Test
DECL|method|roundTripToProto ()
specifier|public
name|void
name|roundTripToProto
parameter_list|()
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|CachedRef
argument_list|>
name|refs
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|refs
operator|.
name|put
argument_list|(
literal|"refs/heads/master"
argument_list|,
operator|new
name|CachedRef
argument_list|(
literal|1
argument_list|,
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|refs
operator|.
name|put
argument_list|(
literal|"refs/heads/branch"
argument_list|,
operator|new
name|CachedRef
argument_list|(
literal|2
argument_list|,
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ObjectIdOwnerMap
argument_list|<
name|Tag
argument_list|>
name|tags
init|=
operator|new
name|ObjectIdOwnerMap
argument_list|<>
argument_list|()
decl_stmt|;
name|tags
operator|.
name|add
argument_list|(
operator|new
name|Tag
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"cccccccccccccccccccccccccccccccccccccccc"
argument_list|)
argument_list|,
name|newBitSet
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|tags
operator|.
name|add
argument_list|(
operator|new
name|Tag
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"dddddddddddddddddddddddddddddddddddddddd"
argument_list|)
argument_list|,
name|newBitSet
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|,
literal|6
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|TagSet
name|tagSet
init|=
operator|new
name|TagSet
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"project"
argument_list|)
argument_list|,
name|refs
argument_list|,
name|tags
argument_list|)
decl_stmt|;
name|TagSetProto
name|proto
init|=
name|tagSet
operator|.
name|toProto
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|proto
argument_list|)
operator|.
name|ignoringRepeatedFieldOrder
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|TagSetProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setProjectName
argument_list|(
literal|"project"
argument_list|)
operator|.
name|putRef
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|CachedRefProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
name|bytes
argument_list|(
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|)
argument_list|)
operator|.
name|setFlag
argument_list|(
literal|1
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|putRef
argument_list|(
literal|"refs/heads/branch"
argument_list|,
name|CachedRefProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
name|bytes
argument_list|(
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|)
argument_list|)
operator|.
name|setFlag
argument_list|(
literal|2
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|addTag
argument_list|(
name|TagProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
name|bytes
argument_list|(
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|,
literal|0xcc
argument_list|)
argument_list|)
operator|.
name|setFlags
argument_list|(
name|bytes
argument_list|(
literal|0x2a
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|addTag
argument_list|(
name|TagProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
name|bytes
argument_list|(
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|,
literal|0xdd
argument_list|)
argument_list|)
operator|.
name|setFlags
argument_list|(
name|bytes
argument_list|(
literal|0x54
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqual
argument_list|(
name|tagSet
argument_list|,
name|TagSet
operator|.
name|fromProto
argument_list|(
name|proto
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// TODO(dborowitz): Find some more common place to put this method, which requires access to
comment|// package-private TagSet details.
DECL|method|assertEqual (@ullable TagSet a, @Nullable TagSet b)
specifier|static
name|void
name|assertEqual
parameter_list|(
annotation|@
name|Nullable
name|TagSet
name|a
parameter_list|,
annotation|@
name|Nullable
name|TagSet
name|b
parameter_list|)
block|{
if|if
condition|(
name|a
operator|==
literal|null
operator|||
name|b
operator|==
literal|null
condition|)
block|{
name|assertWithMessage
argument_list|(
literal|"only one TagSet is null out of\n%s\n%s"
argument_list|,
name|a
argument_list|,
name|b
argument_list|)
operator|.
name|that
argument_list|(
name|a
operator|==
literal|null
operator|&&
name|b
operator|==
literal|null
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
return|return;
block|}
name|assertThat
argument_list|(
name|a
operator|.
name|getProjectName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|b
operator|.
name|getProjectName
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|CachedRef
argument_list|>
name|aRefs
init|=
name|a
operator|.
name|getRefsForTesting
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|CachedRef
argument_list|>
name|bRefs
init|=
name|b
operator|.
name|getRefsForTesting
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|aRefs
operator|.
name|keySet
argument_list|()
argument_list|)
argument_list|)
operator|.
name|named
argument_list|(
literal|"ref name set"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|bRefs
operator|.
name|keySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|aRefs
operator|.
name|keySet
argument_list|()
control|)
block|{
name|CachedRef
name|aRef
init|=
name|aRefs
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|CachedRef
name|bRef
init|=
name|bRefs
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|aRef
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"value of ref %s"
argument_list|,
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|bRef
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|aRef
operator|.
name|flag
argument_list|)
operator|.
name|named
argument_list|(
literal|"flag of ref %s"
argument_list|,
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|bRef
operator|.
name|flag
argument_list|)
expr_stmt|;
block|}
name|ObjectIdOwnerMap
argument_list|<
name|Tag
argument_list|>
name|aTags
init|=
name|a
operator|.
name|getTagsForTesting
argument_list|()
decl_stmt|;
name|ObjectIdOwnerMap
argument_list|<
name|Tag
argument_list|>
name|bTags
init|=
name|b
operator|.
name|getTagsForTesting
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|getTagIds
argument_list|(
name|aTags
argument_list|)
argument_list|)
operator|.
name|named
argument_list|(
literal|"tag ID set"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|getTagIds
argument_list|(
name|bTags
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Tag
name|aTag
range|:
name|aTags
control|)
block|{
name|Tag
name|bTag
init|=
name|bTags
operator|.
name|get
argument_list|(
name|aTag
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|aTag
operator|.
name|refFlags
argument_list|)
operator|.
name|named
argument_list|(
literal|"flags for tag %s"
argument_list|,
name|aTag
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|bTag
operator|.
name|refFlags
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getTagIds (ObjectIdOwnerMap<Tag> bTags)
specifier|private
specifier|static
name|ImmutableSortedSet
argument_list|<
name|String
argument_list|>
name|getTagIds
parameter_list|(
name|ObjectIdOwnerMap
argument_list|<
name|Tag
argument_list|>
name|bTags
parameter_list|)
block|{
return|return
name|Streams
operator|.
name|stream
argument_list|(
name|bTags
argument_list|)
operator|.
name|map
argument_list|(
name|Tag
operator|::
name|name
argument_list|)
operator|.
name|collect
argument_list|(
name|ImmutableSortedSet
operator|.
name|toImmutableSortedSet
argument_list|(
name|Comparator
operator|.
name|naturalOrder
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newBitSet (int... bits)
specifier|private
name|BitSet
name|newBitSet
parameter_list|(
name|int
modifier|...
name|bits
parameter_list|)
block|{
name|BitSet
name|result
init|=
operator|new
name|BitSet
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|stream
argument_list|(
name|bits
argument_list|)
operator|.
name|forEach
argument_list|(
name|result
operator|::
name|set
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit
